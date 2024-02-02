package lk.ijse.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {

//    public static List<ClientHandler> clientHandlerList = new ArrayList<>();
    private static ArrayList<DataOutputStream> clientOutputStreams;
    private  Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String clientName;


    public ClientHandler(Socket socket,ArrayList<DataOutputStream> clientOutputStreams) throws IOException {
        this.socket = socket;
        this.clientOutputStreams = clientOutputStreams;
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
        clientName = inputStream.readUTF();
        clientOutputStreams.add(outputStream);
    }
    @Override
    public void run(){
        while (socket.isConnected()){
            try {
                String message = inputStream.readUTF();
                if (message.equals("image")){
                    receiveImage();
                }else {
                    for (DataOutputStream clientOutputStream : clientOutputStreams){
//                        clientOutputStream.writeUTF("TEXT");
                        if (clientOutputStream != outputStream) { // Exclude sender's output stream
                            clientOutputStream.writeUTF(message);
                            clientOutputStream.flush();
                        }
                    }
                }
            } catch (IOException e) {
//                clientOutputStream.remove(this);
//                throw new RuntimeException(e);
                break;
            }
        }
    }

    private void receiveImage() throws IOException {
        int size = inputStream.readInt();
        byte[] bytes = new byte[size];
        inputStream.readFully(bytes);
        for (DataOutputStream clientOutputStream : clientOutputStreams){
            if (clientOutputStream != outputStream){
                sendImage(clientName,bytes);
            }
        }
    }
    private void sendImage(String clientName, byte[] bytes) throws IOException {
        outputStream.writeUTF("image");
        outputStream.writeUTF(clientName);
        outputStream.writeInt(bytes.length);
        outputStream.write(bytes);
        outputStream.flush();
    }
}

