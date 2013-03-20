import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class server_graffic {
	
	static String home_dir = System.getProperty("user.dir");
	
    static class ClientServant implements Runnable
    {
	    private Socket m_socket;
	    private int m_iID;
	    
	    public ClientServant(Socket socket, int iID)
	    {
	        m_socket = socket;
	        m_iID = iID;
	    }
	
	    public void run()
	    {
	        try
	        {
	        	InputStream is = m_socket.getInputStream();
				OutputStream os = m_socket.getOutputStream();
				DataInputStream in = new DataInputStream(is);
				DataOutputStream out = new DataOutputStream(os);
	        	try
	        	{

		        	while (true)
		        	{
		            	String com_cl=in.readUTF();
		            	if (com_cl.equals("put"))
		            	{
		            		RecvFileClient(in, out);
		            	}
		            	else
		            	{
			            	if (com_cl.equals("get"))
			            	{
			            		SendFileClient(os, out, in);
			            	}
			            	else
			            	{
				            	if (com_cl.equals("cd"))
				            	{
				            		change_dir_server(out, in);
				            	}
				            	else
				            	{
					            	if (com_cl.equals("pwd"))
					            	{
					            		out.writeUTF(System.getProperty("user.dir"));
					            	}
					            	else
					            	{
						            	if (com_cl.equals("ls"))
						            	{
						            		String path = "";
											File f = new File(System.getProperty("user.dir"));
											String[] serverDirList = f.list();
											for (int i = 0; i < serverDirList.length; i++) {
												path = path + serverDirList[i] + "\n";
											}
											out.writeUTF(path);
						            	}
						            	else
						            	{
							            	if (com_cl.equals("quit"))
							            	{
							            		break;
							            	}
							            	else
							            	{
							            		out.writeUTF("Вы ввели не поддерживаемую команду\n");
							            	}
						            	}
					            	}
				            	}
		            		}
		            	}
		            	out.flush();
		            	os.flush();
		        	}
	        	}
	            finally
				{
				    closeStream(os);
				    closeStream(is);
				    closeStream(in);
				    closeStream(out);
				    m_socket.close();
				}
	        }
	        catch (Exception e3)
	        {
	        	System.out.println("Один из клиентов завершил свою работу");
	        }
	    }
    }
    
    public static void closeStream(Closeable stream)
    {
        if (stream != null)
        {
            try
            {
                stream.close();
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }
	
	public static void main(String[] argv) 
	{
		int PORT=40000;
		int st_counter = 0;
		try
		{
			ServerSocket serverSocket = new ServerSocket(PORT);
			System.out.println("Сервер ожидает клиентов...\n");
			while (true)
	        {
                try
                {
                	Socket socket = serverSocket.accept();
					System.out.println("Клиент пытается подключиться\n");
					st_counter++;
					ClientServant servant = new ClientServant(socket, st_counter);
					new Thread(servant).start();
                }
                catch (Exception e)
                {
                	System.out.println("Произошел сбой в работе клиента\n");
                }
              
	        }
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
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
			System.out.println("Входного файла не существует");
			return null;
		}
	}
	
	public static void change_dir_server(DataOutputStream out,DataInputStream in)
	 {
		try
		{
			String strLine = in.readUTF();
			String cur_dir=System.getProperty("user.dir");		
			System.out.println(cur_dir);
			if (strLine.equals("..")){
				if(cur_dir.equals(home_dir))
				{
					System.out.println("вы пытаетесь выйти из корневой директории");
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
			out.writeUTF(cur_dir);
			System.out.println(cur_dir);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	 }
	
	public static void SendFileClient(OutputStream m_out, DataOutputStream out,DataInputStream in)
	{
		try
		{
			String strLine = in.readUTF();
			System.out.println("Клиент хочет получить файл "+ strLine);
			byte[] a;
			a = reading(strLine);
	     	if (a == null)
	     	{
	     		out.writeUTF("Нужного файла не существует");
	     	}
	     	else
	     	{
	     		out.writeUTF("Необходимый файл найден");
	     		String end_f=in.readUTF();
	     		System.out.println("Клиент сохранит файл под именем "+end_f);
	     		int len_a=a.length;
	     		System.out.println("На серверной стороне файл имеет "+len_a+"байт");
	     		System.out.println("Отправка данных...");
	     		m_out.write(a);
	     		System.out.println("Сервер файл отправил\n");    		
	     	}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void RecvFileClient(DataInputStream in,DataOutputStream out)
	{
		try
		{
			String strLine = in.readUTF();
	        System.out.println("Клиент назвал выходной файл "+strLine);
	        File file1 = new File(System.getProperty("user.dir"),strLine);
			while (file1.exists()) {
				strLine = '2'+strLine;
				file1 = new File(System.getProperty("user.dir"), strLine);
			}
			out.writeUTF(strLine);
	        OutputStream out2 = new FileOutputStream(file1);
	        ArrayList<Byte> ar = new ArrayList<Byte>();
			
			while (in.available() != 0) {
				ar.add(in.readByte()); // считываем поток байт
			}
		
			byte[] b  = new byte[ar.size()];
			for (int i = 0; i < ar.size(); i++) {
				b[i] = ar.get(i);
			}
	
	        try {
				out2 = new BufferedOutputStream(out2);
				out2.write(b);// записали в файл
			} 
			finally {
				out2.close();
			}
			int size=b.length;
			System.out.println("Файл записан, записано "+size+" байт\n");
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
}

