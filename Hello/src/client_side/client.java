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
	//��ʾ�Ի�����
	JTextArea showText;
	//�����
	JFrame mainjframe;                                                                                                                                                                                                                                                                                                                                                                                                           
	//�߳�
	Thread thread=null;
	//�����ı���
	JTextField inText;
	//���Ͱ�ť
	JButton sendbtn;
	//�����
	DataOutputStream outstream;
	//������
	DataInputStream instream;
	//�׽���
	Socket socket;
	//��������
	Container con;
	//���ڹ��������  
	JScrollPane jsPane;
	//���廭��
	JPanel jpanel;
	public client(){
		//����������
		mainjframe=new JFrame();
		//������Ž�����
		con=mainjframe.getContentPane();
		showText=new JTextArea();//���������ı���
		/*setEditable(true,false)
		 * ����swing����Ŀ����벻����
		 * ��ʾ�ı��򲻿���
		 * ����ֻ����ʾ�ı�����������ı����ݡ�
		 */
		showText.setEditable(false);
		/*�����ı��Ļ��� ,ֵΪboolean
		 * trueΪ���У�falseΪ������
		 */
		showText.setLineWrap(true);
		jsPane=new JScrollPane(showText);//������ʾ�ı��Ĺ������
		inText=new JTextField();//������ʾ�ı���
		/*
		 * ������ʾ�ı�����������Ϊ�����intֵ
		 * ��ʾ���������
		 */
		inText.setColumns(30);
		/*���ָ����������
		 * �ԴӴ��ı��ֶν��ղ����¼�  
		 */
		inText.addActionListener(this);
		sendbtn=new JButton("����");//�������Ͱ�ť
		sendbtn.addActionListener(this);
		jpanel=new JPanel();//�������
		jpanel.setLayout(new FlowLayout());
		//�����Ͱ�ť�������Ž���������
		jpanel.add(inText);
		jpanel.add(sendbtn);
		//����������ͻ���Ž�������������
		/*
		 * BorderLayout���ʾ���������ı߿򲼾֣������Զ�����������а��ţ����������С��
		 * ʹ���������������򣺱����ϡ����������С�ÿ���������ֻ�ܰ���һ�������
		 * ��ͨ����Ӧ�ĳ������б�ʶ��NORTH��SOUTH��EAST��WEST��CENTER��
		 * ��ʹ�ñ߿򲼾ֽ�һ�������ӵ�������ʱ��Ҫʹ�����������֮һ��
		 * NORTH��ʾ������Ĳ���Լ����SOUTH��ʾ������Ĳ���Լ��
		 * EAST��ʾ������Ĳ���Լ����WEST��ʾ������Ĳ���Լ����CENTER��ʾ��������Ĳ���Լ��
		 */
		con.add(jsPane,BorderLayout.CENTER);
		con.add(jpanel,BorderLayout.SOUTH);
		//���ô�������
		mainjframe.setSize(500,700);
		mainjframe.setVisible(true);
		mainjframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			//��ip��ַ�Ͷ˿ںŴ���һ���׽���socket
			socket=new Socket("localhost",8080);
			//��ȡsocket�����롢�����
			outstream=new DataOutputStream(socket.getOutputStream());
			instream=new DataInputStream(socket.getInputStream());
			//���������ı���ӵ��ĵ��ĺ���
			showText.append("���ӳɹ�����˵��\n");
			//����һ���߳�
			thread=new Thread(this);
			//�����̵߳����ȼ�
			thread.setPriority(Thread.MIN_PRIORITY);
			//�����߳�
			thread.start();
		} catch (UnknownHostException e) {
			showText.append("�Բ���û�����ӵ�������\n");
			/*
			 * �����ı��򲻿�ʹ��
			 * ����������ܽ����κβ�����
			 * ����Ϊtrue(����)��false(������)
			 */
			inText.setEditable(false);
			//ͬ��
			sendbtn.setEnabled(false);
		} catch (IOException e) {
			showText.append("�Բ���û�����ӵ�������\n");
			inText.setEditable(false);
			sendbtn.setEnabled(false);			}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		//��ȡ�ı�����������
		String text=inText.getText();
		if(text.length()>0){
			try {
				//��������޹ط�ʽʹ�� UTF-8 �޸İ���뽫һ���ַ���д����������
				outstream.writeUTF(text);
				//��մ����������������ʹ���л��������ֽڱ�д��������
				outstream.flush();
				//���ĵ���׷������������
				showText.append("�ͻ��� "+inText.getText()+"\n");
				//�������������
				inText.setText(null);
			} catch (IOException e1) {
				showText.append("�����Ϣ"+inText.getText()+"δ�ܷ���"+"\n");
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
				showText.append("�Է�˵ "+instream.readUTF()+"\n");
				Thread.sleep(2000);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
