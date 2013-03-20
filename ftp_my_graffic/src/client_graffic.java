import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.event.*;

public class client_graffic extends JFrame implements ListSelectionListener, ActionListener {
	
	static String home_dir = System.getProperty("user.dir");
	
	public static String readLine() 
	{
	    try 
	    {
	      return new BufferedReader(
	        new InputStreamReader(System.in)).readLine();
	    } 
	    catch (IOException e) 
	    {
	      return new String();
	    }
	}

	public static byte[] reading(String inp_f) throws IOException
	{
		byte[] h;
		try
		{
			File file = new File(System.getProperty("user.dir"),inp_f);
			InputStream is = new FileInputStream(file);
			try 
			{
				is = new BufferedInputStream(is);
				h = new byte[is.available()];
				is.read(h);
			} 
			finally 
			{
				is.close();
			}
			return h;
		}
		catch (FileNotFoundException eee)
		{
			System.out.println("�������� ����� �� ����������");
			return null;
		}
	}
	
	static InetAddress address;
	static Socket socket;
	static OutputStream m_out;
	static DataOutputStream out;
	static InputStream m_in;
	static DataInputStream in;

    static Object name_s;
    static Object name_c;
    static int cl;
    static int ser;
    static JList list_c;
    static JList list_s;
	static DefaultListModel list_c1;
	static DefaultListModel list_s1;	
	static JTextField server;
	static JTextField client;
	static String directory = System.getProperty("user.dir");	
	
	public client_graffic(){
	    try
	    {
		    out.writeUTF("pwd");
		    String str = in.readUTF();
			
			server = new JTextField(str);//��������� �����, ���������� ��������, ������� ����������
			client = new JTextField(directory);
			JLabel label1 = new JLabel("Server");
			JLabel label2 = new JLabel("Client");
			
			server.setCaretPosition(str.length());
		    out.writeUTF("ls");
		    String str1 = in.readUTF();
			String []sDirList_server = new String[str1.split("\n").length];
			sDirList_server=str1.split("\n");
			File f_client = new File(directory);
			String[] sDirList_client =  f_client.list();
			
			list_c1 = new DefaultListModel();
			list_s1 = new DefaultListModel();
			list_s = new JList(list_s1);
	        list_c = new JList(list_c1);
		
	        for (int i = 0; i < sDirList_server.length; i++) {
	            list_s1.addElement(sDirList_server[i]);
	    	}
	        for (int i = 0; i < sDirList_client.length; i++) {
	            list_c1.addElement(sDirList_client[i]);
	    	}
	        
	        list_c.setFixedCellWidth(268);
			list_c.setFixedCellHeight(45);   	
	    	list_c.setVisibleRowCount(8);//���������� ������� ��-��� ������
	        list_s.setFixedCellWidth(268);
			list_s.setFixedCellHeight(45);   	
	    	list_s.setVisibleRowCount(8);//���������� ������� ��-��� ������
	    	// ��� ����� ���������� ����� ��������� ������ ������
	        list_s.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        list_c.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        // ������ ����� �������� ��������� �����
	        list_s.addListSelectionListener(this);
	        list_c.addListSelectionListener(this);
	        JButton s_s = new JButton(">>");
	        JButton s_c = new JButton("<<");
	        JButton s_s_b = new JButton("�����_������");
	        JButton s_c_b = new JButton("�����_������");
	        // ������ ���� ����� �������� ��������� �����
	        s_s.addActionListener(this);
	        s_c.addActionListener(this);
	        s_s_b.addActionListener(this);
	        s_c_b.addActionListener(this);
	        // ����� ����� ������� �����, ����� �� ����� ���� ���������
	        // ��� ���������
	        s_s.setName(">>");
	        s_c.setName("<<");
	        s_s_b.setName("�����_������");
	        s_c_b.setName("�����_������");
	        // �������� ������ ��� ����� ������ � ������� �� layout
	        // � ���� ������� - 1 ������, 2 �������
	        JPanel p = new JPanel();
	        p.setLayout(new GridLayout(1, 4));
	        p.add(s_s_b);
	        p.add(s_s);
	        p.add(s_c_b);
	        p.add(s_c);
	        JPanel p1 = new JPanel();
	        p1.setLayout(new GridLayout(2, 2));
	        p1.add(label1);
	        p1.add(label2);
	        p1.add(server);
	        p1.add(client);
	        JPanel p2 = new JPanel();
	        p2.setLayout(new GridLayout(1, 2));
	        p2.add(new JScrollPane(list_s));
	        p2.add(new JScrollPane(list_c));
	        // ��������� ������ �� ������ ����� 
	        getContentPane().add(p2, BorderLayout.CENTER);
	        // ��������� ������ � ������ �����
	        getContentPane().add(p, BorderLayout.SOUTH);
	        getContentPane().add(p1, BorderLayout.NORTH);
	        // ������������� �������
	        setBounds(100, 100, 500, 500);
        
	    }
	    catch(IOException e)
	    {
	    	e.printStackTrace();
	    }
	}
	
	public static void main(String[] argv) 
	{
		int PORT = 40000;
		try 
		{
			address = InetAddress.getByName("localhost");
			socket = new Socket(address, PORT);
			m_out = socket.getOutputStream();
			out = new DataOutputStream(m_out);
			m_in = socket.getInputStream();
			in = new DataInputStream(m_in);		    
			SwingUtilities.invokeLater(new Runnable() {
		           public void run() {
		                client_graffic t = new client_graffic();
		                t.setDefaultCloseOperation(t.EXIT_ON_CLOSE);
		                t.setVisible(true);
		            }
		        });
        }
        catch (Exception ex)
        {
        	System.out.println("������ �� ��������, �������� � ��� �� �������� ������!\n");
        }
    }
	
	 public static void close(Closeable obj)
	 {
		try
		{
		    obj.close();
		}
		catch (IOException e)
		{
		    e.printStackTrace();
		}
	 }
	 
	 public static void change_dir()
	 {
			System.out.print("������� ��� �������� �� �������, ���� �� ������ �������: ");
			String strLine = readLine();
			String cur_dir=System.getProperty("user.dir");		
			System.out.println(cur_dir);
			if (strLine.equals("..")){
				if(cur_dir.equals(home_dir))
				{
					System.out.println("�� ��������� ����� �� �������� ����������");
				}
				else
				{
					String []a = new String[cur_dir.split("\\\\").length];
					a=cur_dir.split("\\\\");
					cur_dir = "";
					for (int i=0; i<a.length-1; i++)	{
						if (i==a.length-2) cur_dir=cur_dir+a[i];
						else cur_dir=cur_dir+a[i]+"\\";
					}
				}
			}
			else{
				String []ar = new String[cur_dir.split("\\\\").length];
				if(!(strLine.equals(ar[ar.length-1])))
				cur_dir = cur_dir+"\\"+strLine;
			}
			System.setProperty("user.dir", cur_dir);
			System.out.println(cur_dir);			
	 }
	 
	 public static String RecvFileServer(String in_f)
	 {
			try
			{
				out.writeUTF("get");
				out.writeUTF(in_f);
				String strLine=in.readUTF();
				if (strLine.equals("����������� ���� ������"))
				{
					String out_f=in_f;
					File file1 = new File(System.getProperty("user.dir"),out_f);
					while (file1.exists()) {
						out_f = '1'+out_f;
						file1 = new File(System.getProperty("user.dir"), out_f);
					}
					System.out.println("���� ����� �������� ��� ������ "+out_f);
					out.writeUTF(out_f);
					OutputStream out2 = new FileOutputStream(file1);
					ArrayList<Byte> ar = new ArrayList<Byte>();
					while (in.available() != 0) {
						ar.add(in.readByte()); // ��������� ����� ����
					}
					byte[] b  = new byte[ar.size()];
					for (int i = 0; i < ar.size(); i++) {
						b[i] = ar.get(i);
					}
			        try {
						out2 = new BufferedOutputStream(out2);
						out2.write(b);// �������� � ����
					} 
					finally {
						out2.close();
					}
					int size=b.length;
					System.out.println("���� �������, �������� "+size+" ����\n");
					return out_f;
				}
				else
				{
					return "����������� ���� �� ������";
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return null;
			}
	 }
	 
	 public static String SendFileServer(String inp_f)
	 {
		try 
		{
			out.writeUTF("put");
	     	byte[] a;
			a = reading(inp_f);
	     	if (a == null)
	     	{
	     		return "������� ����� �� ����������, ������ ���������� ������";
	     	}
	     	else
	     	{
	     		String out_f=inp_f;
	     		out.writeUTF(out_f);
	     		int len_a=a.length;
	     		out_f = in.readUTF();
	     		System.out.println("�� ���������� ������� ���� ����� "+len_a+"����");
	     		System.out.println("�������� ������...");
	     		m_out.write(a);
	     		System.out.println("������ ���������\n");  
	     		return out_f;
	     	}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	 }

	public void actionPerformed(ActionEvent e) {
		
	try
	{
        JButton sender = (JButton) e.getSource();
        if (sender.getName().equals(">>")) {
           	String re = "\\w+\\.\\w*";
        	String d = (String) name_s;
			boolean ok = Pattern.matches(re,d);//����������
			String h = "";
			if (ok){
				
				h = RecvFileServer(d);
				if (!h.equals("������� ����� �� ����������"))
				{
				String [] name_list = new String[h.split("\\\\").length];
				name_list = h.split("\\\\");
				h=name_list[name_list.length-1];			
				list_c1.addElement(h);
				}
				else
				{
					System.out.println(h);
				}
			}
			else{								
	        	String de = (String) name_s;
	        	File fl = new File(de);
				if (fl.isDirectory())
				{
					String inp_d = de;// ��������� ��� ����������
					out.writeUTF("cd");
					out.writeUTF(inp_d);
					String f = in.readUTF();
					out.writeUTF("pwd");
					String str2 = in.readUTF();
					server.setText(str2);
					out.writeUTF("ls");
					String str3 = in.readUTF();
					String []sDirList_server = new String[str3.split("\n").length];
					sDirList_server=str3.split("\n");
					list_s1.removeAllElements();
			    	for (int i = 0; i < sDirList_server.length; i++) {
			            list_s1.addElement(sDirList_server[i]);
			    	}
				}
				else
				{
					System.out.println("������������ ������ �����");
				}
			}        		
        }
       if (sender.getName().equals("<<") ) {
    	   	String re = "\\w+\\.\\w*";
       		String d = (String) name_c;
			boolean ok = Pattern.matches(re,d);//����������
			String h = "";
			if (ok){
				h = SendFileServer(d);
				if (!h.equals("������� ����� �� ����������, ������ ���������� ������"))
				{
					list_s1.addElement(h);
				}
				else
				{
					System.out.println(h);
				}
				
			}
			else{
				String de = (String) name_c;
	        	File fl = new File(de);
				if (fl.isDirectory())
				{
					String inp_d = de;// ��������� ��� ����������
					String []ar = new String[directory.split("\\\\").length];
					if(!(inp_d.equals(ar[ar.length-1])))
					directory = directory+"\\"+inp_d;
					client.setText(directory);
					File f_client = new File(directory);
					String[] sDirList_client =  f_client.list();
					list_c1.removeAllElements();
			    	for (int i = 0; i < sDirList_client.length; i++) {
			            list_c1.addElement(sDirList_client[i]);
			    	}
			    }
				else
				{
					System.out.println("������������ ������ �����");
				}
			}
    	}

       if (sender.getName().equals("�����_������") ){
    	   
    	   out.writeUTF("dir");
    	   String str4 = in.readUTF();
    	   String p = server.getText();
  
    	   if (str4.equals(p))
    	   {
    		   System.out.print("���������� ����� �� �������� ����������");
    	   }
    	   else
    	   {   
				out.writeUTF("cd");
				out.writeUTF("..");
				String f = in.readUTF();
				out.writeUTF("pwd");
				String str2 = in.readUTF();
				server.setText(str2);
				out.writeUTF("ls");
				String str3 = in.readUTF();
				String []sDirList_server = new String[str3.split("\n").length];
				sDirList_server=str3.split("\n");
				list_s1.removeAllElements();
		    	for (int i = 0; i < sDirList_server.length; i++) {
		            list_s1.addElement(sDirList_server[i]);
		    	}
    	   }
       }
       
       if (sender.getName().equals("�����_������") ){
    	   
    	   String str5 = System.getProperty("user.dir");
    	   if (str5.equals(directory))
    	   {
    		   System.out.print("���������� ����� �� �������� ����������");
    	   }
    	   else{
    	   
	    	   String inp_d = "..";// ��������� ��� ����������
			   String []a = new String[directory.split("\\\\").length];
			   a=directory.split("\\\\");
			   directory = "";
			   for (int i=0; i<a.length-1; i++)	{
				   if (i==a.length-2) directory=directory+a[i];
				   else	directory=directory+a[i]+"\\";
			   }
			
			   client.setText(directory);
			   
			   File f_client = new File(directory);
			   String[] sDirList_client =  f_client.list();
			   list_c1.removeAllElements();
			   for (int i = 0; i < sDirList_client.length; i++) {
				   list_c1.addElement(sDirList_client[i]);
			   }
    	   }
       }
	}
	catch (IOException e1)
	{
		e1.printStackTrace();
	}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
        	name_s = null;
        	name_c = null;
            System.out.println("New index:" + list_s.getSelectedIndex());
            System.out.println("New index:" + list_c.getSelectedIndex());
            System.out.print("!!!"+list_s.getSelectedValue()+"  s\n");
            System.out.print("!!!"+list_c.getSelectedValue()+"  c\n");
            name_s = list_s.getSelectedValue();
            name_c = list_c.getSelectedValue();
        }
	}
	 
}	
