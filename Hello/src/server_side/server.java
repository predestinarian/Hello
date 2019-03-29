package server_side;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
public class server implements ActionListener,Runnable {
	//显示对话内容
	JTextArea showText;
	//主框架
	JFrame mainjframe;
	//线程
	Thread thread=null;
	//输入文本框
	JTextField inText;
	//发送按钮
	JButton sendbtn;
	//输出流
	DataOutputStream outstream;
	//输入流
	DataInputStream instream;
	//服务端的套接字
	ServerSocket serversocket;
	//套接字
	Socket scoket;
	//窗口容器
	Container con;
	//窗口滚动条组件  
	JScrollPane jsPane;
	//窗体画布
	JPanel jpanel;
	public server(){
		//创建主窗体
		mainjframe=new JFrame();
		//将窗体放进容器
		con=mainjframe.getContentPane();
		showText=new JTextArea();//创建输入文本域
		showText.setEditable(false);
		/*设置文本的换行 ,值为boolean
		 * true为换行，false为不换行
		 */
		showText.setLineWrap(true);
		jsPane=new JScrollPane(showText);//创建显示文本的滚动面板
		inText=new JTextField();//创建显示文本域
		/*
		 * 设置显示文本列数，参数为具体的int值
		 * 表示具体的列数
		 */
		inText.setColumns(40);
		/*添加指定的侦听器
		 * 以从此文本字段接收操作事件  
		 */
		inText.addActionListener(this);
		sendbtn=new JButton("发送");//创建发送按钮
		sendbtn.addActionListener(this);
		jpanel=new JPanel();//创建面板
		jpanel.setLayout(new FlowLayout());
		jpanel.add(inText);
		jpanel.add(sendbtn);
		con.add(jsPane,BorderLayout.CENTER);
		con.add(jpanel,BorderLayout.SOUTH);
		mainjframe.setSize(500,700);
		mainjframe.setVisible(true);
		mainjframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			serversocket=new ServerSocket(8080);
			showText.append("正在等待对方的连接\n");
			/* serversocket是服务端的套接字。
			 * 侦听并接受到此套接字的连接。未连接时，进入阻塞状态
			 * 与客户端socket连接后，自动解除阻塞
			 */
			scoket=serversocket.accept();
			outstream=new DataOutputStream(scoket.getOutputStream());
			instream=new DataInputStream(scoket.getInputStream());
			thread=new Thread(this);
			thread.setPriority(Thread.MIN_PRIORITY);
			thread.start();
		} catch (IOException e) {
			showText.append("对不起，没有连接失败\n");
		}
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String text=inText.getText();
		if(text.length()>0){
			try {
				outstream.writeUTF(text);
				outstream.flush();
				showText.append("服务端  "+inText.getText()+"\n");
				inText.setText(null);
			} catch (IOException e1) {
				showText.append("你的消息"+inText.getText()+"未能发送"+"\n");
			}
		}

	}
	public static void main(String[] args) {
		new server();
	}
	@Override
	public void run() {
		while(true){
			try {
				showText.append("对方说 "+instream.readUTF()+"\n");
				Thread.sleep(2000);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
