package client_side;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
public class client implements ActionListener,Runnable {
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
	//套接字
	Socket socket;
	//窗口容器
	Container con;
	//窗口滚动条组件  
	JScrollPane jsPane;
	//窗体画布
	JPanel jpanel;
	public client(){
		//创建主窗体
		mainjframe=new JFrame();
		//将窗体放进容器
		con=mainjframe.getContentPane();
		showText=new JTextArea();//创建输入文本域
		/*setEditable(true,false)
		 * 设置swing组件的可用与不可用
		 * 显示文本框不可用
		 * 即，只能显示文本，不能添加文本内容。
		 */
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
		inText.setColumns(30);
		/*添加指定的侦听器
		 * 以从此文本字段接收操作事件  
		 */
		inText.addActionListener(this);
		sendbtn=new JButton("发送");//创建发送按钮
		sendbtn.addActionListener(this);
		jpanel=new JPanel();//创建面板
		jpanel.setLayout(new FlowLayout());
		//将发送按钮和输入框放进画板里面
		jpanel.add(inText);
		jpanel.add(sendbtn);
		//将滚动组件和画板放进窗体容器里面
		/*
		 * BorderLayout类表示布置容器的边框布局，它可以对容器组件进行安排，并调整其大小，
		 * 使其符合下列五个区域：北、南、东、西、中。每个区域最多只能包含一个组件，
		 * 并通过相应的常量进行标识：NORTH、SOUTH、EAST、WEST、CENTER。
		 * 当使用边框布局将一个组件添加到容器中时，要使用这五个常量之一，
		 * NORTH表示北区域的布局约束，SOUTH表示南区域的布局约束
		 * EAST表示东区域的布局约束，WEST表示西区域的布局约束，CENTER表示中央区域的布局约束
		 */
		con.add(jsPane,BorderLayout.CENTER);
		con.add(jpanel,BorderLayout.SOUTH);
		//设置窗体属性
		mainjframe.setSize(500,700);
		mainjframe.setVisible(true);
		mainjframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			//用ip地址和端口号创建一个套接字socket
			socket=new Socket("localhost",8080);
			//获取socket的输入、输出流
			outstream=new DataOutputStream(socket.getOutputStream());
			instream=new DataInputStream(socket.getInputStream());
			//将给定的文本添加到文档的后面
			showText.append("连接成功，请说话\n");
			//创建一个线程
			thread=new Thread(this);
			//设置线程的优先级
			thread.setPriority(Thread.MIN_PRIORITY);
			//启动线程
			thread.start();
		} catch (UnknownHostException e) {
			showText.append("对不起，没有连接到服务器\n");
			/*
			 * 输入文本框不可使用
			 * 即，组件不能进行任何操作，
			 * 参数为true(可用)或false(不可用)
			 */
			inText.setEditable(false);
			//同上
			sendbtn.setEnabled(false);
		} catch (IOException e) {
			showText.append("对不起，没有连接到服务器\n");
			inText.setEditable(false);
			sendbtn.setEnabled(false);			}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		//获取文本输入框的内容
		String text=inText.getText();
		if(text.length()>0){
			try {
				//以与机器无关方式使用 UTF-8 修改版编码将一个字符串写入基础输出流
				outstream.writeUTF(text);
				//清空此数据输出流。这迫使所有缓冲的输出字节被写出到流中
				outstream.flush();
				//在文档中追加输入框的内容
				showText.append("客户端 "+inText.getText()+"\n");
				//清空输入框的内容
				inText.setText(null);
			} catch (IOException e1) {
				showText.append("你的消息"+inText.getText()+"未能发送"+"\n");
			}
		}

	}
	public static void main(String[] args) {
		new client();
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
