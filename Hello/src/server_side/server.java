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
	//����˵��׽���
	ServerSocket serversocket;
	//�׽���
	Socket scoket;
	//��������
	Container con;
	//���ڹ��������  
	JScrollPane jsPane;
	//���廭��
	JPanel jpanel;
	public server(){
		//����������
		mainjframe=new JFrame();
		//������Ž�����
		con=mainjframe.getContentPane();
		showText=new JTextArea();//���������ı���
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
		inText.setColumns(40);
		/*���ָ����������
		 * �ԴӴ��ı��ֶν��ղ����¼�  
		 */
		inText.addActionListener(this);
		sendbtn=new JButton("����");//�������Ͱ�ť
		sendbtn.addActionListener(this);
		jpanel=new JPanel();//�������
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
			showText.append("���ڵȴ��Է�������\n");
			/* serversocket�Ƿ���˵��׽��֡�
			 * ���������ܵ����׽��ֵ����ӡ�δ����ʱ����������״̬
			 * ��ͻ���socket���Ӻ��Զ��������
			 */
			scoket=serversocket.accept();
			outstream=new DataOutputStream(scoket.getOutputStream());
			instream=new DataInputStream(scoket.getInputStream());
			thread=new Thread(this);
			thread.setPriority(Thread.MIN_PRIORITY);
			thread.start();
		} catch (IOException e) {
			showText.append("�Բ���û������ʧ��\n");
		}
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String text=inText.getText();
		if(text.length()>0){
			try {
				outstream.writeUTF(text);
				outstream.flush();
				showText.append("�����  "+inText.getText()+"\n");
				inText.setText(null);
			} catch (IOException e1) {
				showText.append("�����Ϣ"+inText.getText()+"δ�ܷ���"+"\n");
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
