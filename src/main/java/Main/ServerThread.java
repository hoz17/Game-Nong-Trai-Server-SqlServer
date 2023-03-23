package Main;

import DAO.UserDAO;
import Model.User;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;



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
//        System.out.println("Server thread number " + clientNumber + " Started");
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
        try {
            // Mở luồng vào ra trên Socket tại Server.
            is = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
            os = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));
//            System.out.println("Khời động luông mới thành công, ID là: " + clientNumber);
            write("server-send-id" + "=" + this.clientNumber);
            String message;
            while (!isClosed) {
                message = is.readLine();
                if (message == null) {
                    break;
                }
                String[] messageSplit = message.split("=");
                //Xác minh
                if (messageSplit[0].equals("client-verify")) {
//                    System.out.println(message);
                    User user1 = userDAO.verifyUser(new User(messageSplit[1], messageSplit[2]));
                    if (user1 == null)
                        write("wrong-user=" + messageSplit[1] + "=" + messageSplit[2]);
                    else
//                        if (!user1.getIsOnline() && !userDAO.checkIsBanned(user1))
                    {
                        write("login-success=" + getStringFromUser(user1));
                        this.user = user1;
//                        userDAO.updateToOnline(this.user.getID());
//                        Server.serverThreadBus.boardCast(clientNumber, "chat-server," + user1.getNickname() + " đang online");
//                        Server.admin.addMessage("[" + user1.getID() + "] " + user1.getNickname() + " đang online");
                        System.out.println("[" + user1.getUserID() + "] " + user1.getUsername() + " đang online");
                    }
//                    else if (!userDAO.checkIsBanned(user1)) {
//                        write("dupplicate-login," + messageSplit[1] + "," + messageSplit[2]);
//                    }
                }
                //Xử lý đăng kí
                if(messageSplit[0].equals("register")){
                    boolean checkdup = userDAO.checkDuplicated(messageSplit[1]);
                    if(checkdup) write("duplicate-username=");
                    else{
                        User userRegister = new User(messageSplit[1], messageSplit[2], messageSplit[3],Integer.parseInt(messageSplit[4]),Integer.parseInt(messageSplit[5]),Integer.parseInt(messageSplit[6]));
                        userDAO.addUser(userRegister);
                        User userRegistered = userDAO.verifyUser(userRegister);
                        this.user = userRegistered;
                        //userDAO.updateToOnline(this.user.getID());
                        //Server.serverThreadBus.boardCast(clientNumber, "chat-server,"+this.user.getUsername()+" đang online");
                        System.out.println("[" + this.user.getUserID() + "] " + this.user.getUsername() + " đang online");
                        write("login-success="+getStringFromUser(this.user));
                    }
                }
                //




            }
        } catch (IOException e) {
            //Thay đổi giá trị cờ để thoát luồng
            isClosed = true;
            //Cập nhật trạng thái của user
            if (this.user != null) {
//                userDAO.updateToOffline(this.user.getUser_ID());
//                userDAO.updateToNotPlaying(this.user.getUser_ID());
//                Server.serverThreadBus.boardCast(clientNumber, "chat-server," + this.user.getUsername() + " đã offline");
                System.out.println("[" + user.getUserID() + "] " + user.getUsername() + " đã offline");
                //Server.admin.addMessage("[" + user.getUser_ID() + "] " + user.getNickname() + " đã offline");
            }

            //remove thread khỏi bus
            Server.serverThreadBus.remove(clientNumber);
            System.out.println(this.clientNumber + " đã thoát");

        } catch (SQLException e) {
//            throw new RuntimeException(e);
        }
    }


    public String getStringFromUser(User user1) {
        return "" + user1.getUserID() + "=" + user1.getUsername()
                + "=" + user1.getPassword() + "";
    }

    public void write(String message) throws IOException {
        os.write(message);
        os.newLine();
        os.flush();
    }
}
