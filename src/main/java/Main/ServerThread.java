package Main;

import DAO.UserDAO;
import Model.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

public class ServerThread implements Runnable {
    private User user;
    private Socket socketOfServer;
    private int clientNumber;
    private BufferedReader is;
    private BufferedWriter os;
    private boolean isClosed;
    private UserDAO userDAO;
    private String clientIP;

    public BufferedReader getIs() {
        return is;
    }

    public BufferedWriter getOs() {
        return os;
    }

    public int getClientNumber() {
        return clientNumber;
    }

    public User getUser() {
        return user;
    }

    public String getClientIP() {
        return clientIP;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ServerThread(Socket socketOfServer, int clientNumber) {
        this.socketOfServer = socketOfServer;
        this.clientNumber = clientNumber;
        System.out.println("Server thread number " + clientNumber + " Started");
        userDAO = new UserDAO();
        isClosed = false;
        //Trường hợp test máy ở server sẽ lỗi do hostaddress là localhost
        if (this.socketOfServer.getInetAddress().getHostAddress().equals("127.0.0.1")) {
            clientIP = "127.0.0.1";
        } else {
            clientIP = this.socketOfServer.getInetAddress().getHostAddress();
        }

    }

    @Override
    public void run() {
//        try{
//            // Mở luồng vào ra trên Socket tại Server.
//            is = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
//            os = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));
//            System.out.println("Khời động luông mới thành công, ID là: " + clientNumber);
//            write("server-send-id" + "," + this.clientNumber);
//            String message;
//            while (!isClosed) {
//                message = is.readLine();
//                if (message == null) {
//                    break;
//                }
//                String[] messageSplit = message.split(",");
//                //Xác minh
//                if(messageSplit[0].equals("client-verify")){
//                    System.out.println(message);
//                    User user1 = userDAO.verifyUser(new User(messageSplit[1], messageSplit[2]));
//
//        }catch (){
//
//        }

    }

    public String getStringFromUser(User user1) {
        return "" + user1.getUser_ID() + "," + user1.getUsername()
                + "," + user1.getPassword() + "";
    }

    public void write(String message) throws IOException {
        os.write(message);
        os.newLine();
        os.flush();
    }
}
