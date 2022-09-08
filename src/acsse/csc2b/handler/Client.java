package acsse.csc2b.handler;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JOptionPane;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;

public class Client extends HBox{
	private Socket socket;
	private int portNumber =  46829;
	private DataInputStream din = null;
	private DataOutputStream dos = null;
	private PrintWriter write = null;
	private TextArea list = null;
	private ImageView img = null;
	private int count = 0;
	
	/**
	 * 
	 * @param strNum the string to be checked
	 * @return true if input is numeric
	 */
	public boolean isNumeric(String strNum) {
		// return false if string is null
	    if (strNum == null) {
	        return false;
	    }
	    // catch a not number exception and return false
	    try {
	        @SuppressWarnings("unused")
			double d = Double.parseDouble(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}

	public Client()
	{
		// setting up my javaFX user interface
		this.setBackground(Background.fill(Color.GRAY));
		VBox btnBox = new VBox();
		VBox ImageBox = new VBox();
		ImageBox.setBorder(Border.stroke(Color.BLACK));
				
		ImageBox.setMaxHeight(500);
		ImageBox.setMaxWidth(500);
		
		ImageBox.setMaxSize(500,500);
		ImageBox.setMinSize(500, 500);		
		
		Button btnList = new Button("Get Image List");
		Button btnImage = new Button("Get Image By ID");			
		Button btnAdd = new Button("Add Image");
		
		btnList.setMaxSize(150, 30);
		btnImage.setMaxSize(150, 30);
		btnAdd.setMaxSize(150, 30);
		
		btnList.setMinSize(150, 30);
		btnImage.setMinSize(150, 30);
		btnAdd.setMinSize(150, 30);
		
		btnBox.setSpacing(30);
		this.setSpacing(10);
			
		// this button is responsible for getting an image for the server
		btnImage.setOnAction(Event->{			
			try {
				if(socket == null) {
					
					// setting up the socket and streams
					socket = new Socket("localhost",portNumber);
					write = new PrintWriter(socket.getOutputStream());
					dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
					din = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
					
					// command for sending an image
					dos.writeUTF("GETIMG");
					dos.flush();
					
					String ID = "";
					do {
						ID = JOptionPane.showInputDialog("Please enter the image ID"); // getting image id for user
					}
					while(!isNumeric(ID));
					
					dos.writeUTF(ID);
					dos.flush();
					
					// getting the filename for the server
					String filename = din.readUTF();
					if(filename.equals("File Not Found")) //file does not exist so we handle error
					{
						if(count>0)
						{
							ImageBox.getChildren().remove(0); // remove any node for the stage to create space for the new node
						}
						
						Text txt = new Text("File \nNot \nFound"); //text to be printed if file is not found
						txt.setX(500);
						txt.setY(500);
						txt.setTextAlignment(TextAlignment.CENTER);
						txt.setFont(Font.font ("arial", 50));
					
						ImageBox.getChildren().add(txt);
						count++;
					}
					else
					{
						// this executes if the file is found
						FileOutputStream fos = new FileOutputStream(new File("data/client",filename)); // create a file on our client side.
						
						//read in a file from the data input stream
						long fileSize = din.readLong();
						byte[] buffer = new byte[1024];
						int n=0;
						int totalbytes = 0;
						while(totalbytes!=fileSize)
						{
							n= din.read(buffer,0, buffer.length);
							fos.write(buffer,0,n);
							fos.flush();
							totalbytes+=n;
						}
						fos.close();
						
						
						// after getting the image we display it on our client side.
						if(count>0)
						{
							ImageBox.getChildren().remove(0);
						}
							
						Image image = new Image("file:data/client/"+filename);
						
						img = null;
						img = new ImageView(image);
						
						// resize the image to fit our image view
						img.setX(500);
						img.setY(500);
						img.maxHeight(500);
						img.maxWidth(500);
				
					    img.setFitWidth(498);
					    img.setFitHeight(498);
					    img.setPreserveRatio(true);
						
						ImageBox.getChildren().add(img);
						count++;
					}

					// close all streams opened
					socket.close();
					write.close();
					dos.close();
					din.close();
				}
				else
				{
					socket = null;
					write = null;
					dos = null;
					din = null;
					
					// setting up the socket and streams
					socket = new Socket("localhost",portNumber);
					write = new PrintWriter(socket.getOutputStream());
					dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
					din = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
					
					// command for sending an image
					dos.writeUTF("GETIMG");
					dos.flush();
					
					
					String ID = "";
					do {
						ID = JOptionPane.showInputDialog("Please enter the image ID"); // getting image id for user
					}
					while(!isNumeric(ID));
					
					dos.writeUTF(ID);
					dos.flush();
					
					// getting the filename for the server
					String filename = din.readUTF();
					if(filename.equals("File Not Found")) //file does not exist so we handle error
					{
						if(count>0)
						{
							ImageBox.getChildren().remove(0); // remove any node for the stage to create space for the new node
						}
						
						Text txt = new Text("File \nNot \nFound"); //text to be printed if file is not found
						txt.setX(500);
						txt.setY(500);
						txt.setTextAlignment(TextAlignment.CENTER);
						txt.setFont(Font.font ("arial", 50));
					
						ImageBox.getChildren().add(txt);
						count++;
					}
					else
					{
						// this executes if the file is found
						FileOutputStream fos = new FileOutputStream(new File("data/client",filename)); // create a file on our client side.
						
						//read in a file from the data input stream
						long fileSize = din.readLong();
						byte[] buffer = new byte[1024];
						int n=0;
						int totalbytes = 0;
						while(totalbytes!=fileSize)
						{
							n= din.read(buffer,0, buffer.length);
							fos.write(buffer,0,n);
							fos.flush();
							totalbytes+=n;
						}
						fos.close();
						
						
						// after getting the image we display it on our client side.
						if(count>0)
						{
							ImageBox.getChildren().remove(0);
						}
							
						Image image = new Image("file:data/client/"+filename);
						
						img = null;
						img = new ImageView(image);
						
						// resize the image to fit our image view
						img.setX(500);
						img.setY(500);
						img.maxHeight(500);
						img.maxWidth(500);
				
					    img.setFitWidth(498);
					    img.setFitHeight(498);
					    img.setPreserveRatio(true);
						
						ImageBox.getChildren().add(img);
						count++;
					}

					// close all streams opened
					socket.close();
					write.close();
					dos.close();
					din.close();
				}
				
			} catch(UnknownHostException e)
			{
				System.err.println("Connection failed: UnkownHost Exception");
			}
			catch(ConnectException e)
			{
				System.err.println("Failed to connect");
				e.getMessage();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		});
		
		btnList.setOnAction(Event->{
			// this button handles the event of getting 
			try {
				if(socket == null) {
					socket = new Socket("localhost",portNumber);
					write = new PrintWriter(socket.getOutputStream());
					dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
					din = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
								
					dos.writeUTF("IMGLST"); // list command
					dos.flush();
					
					String filename = din.readUTF(); // a list file name is read from server
					
					FileOutputStream fos = new FileOutputStream(new File("data/client",filename));
					
					// reading in all the bytes of the file to the file size
					long fileSize = din.readLong();
					byte[] buffer = new byte[1024];
					int n=0;
					int totalbytes = 0;
					while(totalbytes!=fileSize)
					{
						n= din.read(buffer,0, buffer.length);
						fos.write(buffer,0,n);
						fos.flush();
						totalbytes+=n;
					}
					fos.close();
					
					//reading newly added image list
					BufferedReader read = new BufferedReader(new FileReader("data/client/Image-List.txt"));
					
					String file = "";
					list = new TextArea();
					list.setMinSize(500, 500);
					list.setMaxSize(500, 500);
					
					// writing list to the display node
					while((file = read.readLine())!=null)
					{
						list.appendText(file +"\n");
					}
					read.close();
					
					if(count>0)
					{
						ImageBox.getChildren().remove(0); // any node available 
					}
					
					ImageBox.getChildren().add(list); /// add our list to to the GUI
					count++;
					
					
					//close socket and open streams
					socket.close();
					write.close();
					dos.close();
					din.close();
				}
				else
				{
					socket = null;
					write = null;
					dos = null;
					
					socket = new Socket("localhost",portNumber);
					write = new PrintWriter(socket.getOutputStream());
					dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
					din = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
								
					dos.writeUTF("IMGLST"); // list command
					dos.flush();
					
					String filename = din.readUTF(); // a list file name is read from server
					
					FileOutputStream fos = new FileOutputStream(new File("data/client",filename));
					
					// reading in all the bytes of the file to the file size
					long fileSize = din.readLong();
					byte[] buffer = new byte[1024];
					int n=0;
					int totalbytes = 0;
					while(totalbytes!=fileSize)
					{
						n= din.read(buffer,0, buffer.length);
						fos.write(buffer,0,n);
						fos.flush();
						totalbytes+=n;
					}
					fos.close();
					
					//reading newly added image list
					BufferedReader read = new BufferedReader(new FileReader("data/client/Image-List.txt"));
					
					String file = "";
					list = new TextArea();
					list.setMinSize(500, 500);
					list.setMaxSize(500, 500);
					
					// writing list to the display node
					while((file = read.readLine())!=null)
					{
						list.appendText(file +"\n");
					}
					read.close();
					
					if(count>0)
					{
						ImageBox.getChildren().remove(0); // any node available 
					}
					
					ImageBox.getChildren().add(list); /// add our list to to the GUI
					count++;
					
					
					//close socket and open streams
					socket.close();
					write.close();
					dos.close();
					din.close();
				}
				
			}catch(UnknownHostException e)
			{
				System.err.println("Connection failed: UnkownHost Exception");
			}
			catch(ConnectException e)
			{
				System.err.println("Failed to connect");
				e.getMessage();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		// this buttons adds an image to the server
		btnAdd.setOnAction(Event->{
			
			//choose an image to send to the server
			FileChooser fc = new FileChooser();
			fc.setTitle("Choose an Image");
			fc.setInitialDirectory(new File("data/client"));
			File file = fc.showOpenDialog(null);
				
			// file size of image we got.
			long fileSize = file.length();
			String filename = file.getName();	
			
			try {
				if(socket == null) {
					socket = new Socket("localhost",portNumber);
					write = new PrintWriter(socket.getOutputStream());
					dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
								
					dos.writeUTF("POSTIMG"); // adding image command
					dos.writeUTF(filename);
					dos.writeLong(fileSize);
					
					// create file to send over 
					FileInputStream fis = new FileInputStream(file);
					
					// sending file bytes over socket stream
					while(fis.available()>0)
					{
						dos.write(fis.readAllBytes());
					}
					
					Scanner scan = new Scanner(new BufferedInputStream(socket.getInputStream()));
					JOptionPane.showMessageDialog(null,scan.nextLine()); // get an Ok message from server for successful image addition
					
					//close all open streams
					scan.close();
					fis.close();
					socket.close();
					write.close();
					dos.close();
				}
				else
				{
					socket = null;
					write = null;
					dos = null;
					
					socket = new Socket("localhost",portNumber);
					write = new PrintWriter(socket.getOutputStream());
					dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
								
					dos.writeUTF("POSTIMG"); // adding image command
					dos.writeUTF(filename);
					dos.writeLong(fileSize);
					
					// create file to send over 
					FileInputStream fis = new FileInputStream(file);
					
					// sending file bytes over socket stream
					while(fis.available()>0)
					{
						dos.write(fis.readAllBytes());
					}
					
					Scanner scan = new Scanner(new BufferedInputStream(socket.getInputStream()));
					JOptionPane.showMessageDialog(null,scan.nextLine()); // get an Ok message from server for successful image addition
					
					//close all open streams
					scan.close();
					fis.close();
					socket.close();
					write.close();
					dos.close();
				}
				
			}catch(UnknownHostException e)
			{
				System.err.println("Connection failed: UnkownHost Exception");
			}
			catch(ConnectException e)
			{
				System.err.println("Failed to connect");
				e.getMessage();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		
		btnBox.getChildren().addAll(btnList,btnImage,btnAdd);
		
		this.getChildren().addAll(btnBox,ImageBox);	
	}
	
	public Client(int portNumber)
	{
		this.portNumber = portNumber;
	}
}
