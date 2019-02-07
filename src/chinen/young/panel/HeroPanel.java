package chinen.young.panel;


import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import chinen.young.rect.CreateRect;

public class HeroPanel extends JPanel 
{
	//���屳��ͼƬ
	private BufferedImage background;//����ͼƬ
	private BufferedImage hero;//����ͼƬ
	private boolean start;//��ʼ
	private boolean end;//����
	private boolean drawline;//�ж��Ƿ��ڻ���״̬
	private boolean revoleline;//�ж��Ƿ�����ת��״̬
	private boolean herorun;//�ж������Ƿ��ƶ�
	private boolean bk;//�жϱ����Ƿ��ƶ�
	private static final int height=474;//ʼ���ڴ�����
	private int length;//��ǻ��ߵĳ���
	private int width;//�������תʱ��x����
	private int h;//�������תʱ��y����
	private int heropoint;//����λ��
	private int drop;//�����½�
	private int score;//����
	private int herowidth;//���ڱ�������С
	private int heroheight;
	private int[] firstRect;//��Ϸ�����ϵĵ�һ������
	private int[] nextRect;//��Ϸ�����ϵĵڶ�������
	private Thread t;//�̱߳��� ������Ϸ
	public HeroPanel()
	{
		try 
		{
			background=ImageIO.read(new File("res/bk.jpg"));
			hero=ImageIO.read(new File("res/hero.jpg"));
			start=false;
			end=false;
			drawline=false;
			revoleline=false;
			herorun=false;
			bk=false;
			length=0;
			width=0;
			h=0;
			score=0;
			heropoint=80;
			drop=0;
			herowidth=hero.getWidth();
			heroheight=hero.getHeight();
			firstRect=new int[]{0,80};
			nextRect=new int[2];
			nextRect=CreateRect.createRect();
			
			addMouseListener(new MyMouseListener());
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
	}
	public void paint(Graphics g)
	{
		super.paint(g);
		
		g.drawImage(background, 0, 0, null);//������ͼ
		Font font=new Font("����",Font.BOLD,30);
		g.setFont(font);
		
		if(!start&&!end)//��Ϸ��û�п�ʼ
		{
			//����ʼ����
			g.drawString("Strat Game", 140,300);
		}
		
		if(start&& !bk)//��Ϸ��ʼ�˵���û���ƶ�����
		{
			g.fillRect(firstRect[0], height, firstRect[1]-firstRect[0], 200);
			g.fillRect(nextRect[0], height, nextRect[1]-nextRect[0], 200);
			g.drawString("Score:"+score, 150, 70);
		}
		
		if(!herorun&&start)//��ʼ������λ�ñ��ֲ���
		{
			g.drawImage(hero, heropoint-herowidth, height-heroheight, null);
		}
		
		if(drawline)//����(����)
		{
			g.drawLine(80, height, 80, height-length);
		}
		
		if(revoleline)//����(��ת)
		{
			g.drawLine(80, height, 80+width, height-h);
		}
		
		if(herorun)//�����ƶ�
		{
			g.drawLine(80, height, 80+length, height);
			g.drawImage(hero, heropoint-herowidth, height-heroheight, null);
		}
		
		if(bk)//�����ƶ����������� ���� ������
		{
			g.fillRect(firstRect[0], height, firstRect[1]-firstRect[0], 200);
			g.fillRect(nextRect[0], height, nextRect[1]-nextRect[0], 200);
			g.drawImage(hero, heropoint-herowidth, height-heroheight, null);
			g.drawString("Score:"+score, 150, 70);
		}
		
		if(!start && end)
		{
			g.fillRect(firstRect[0], height, firstRect[1]-firstRect[0], 200);
			g.fillRect(nextRect[0], height, nextRect[1]-nextRect[0], 200);
			g.drawString("����:"+score, 150, 70);
			g.drawString("Game Over", 140, 300);
			g.drawImage(hero, heropoint-herowidth, height-heroheight+20*drop++, null);
			g.drawLine(80, height, 80+width, height+h);
		}
		
	}
	//������
	class MyMouseListener extends MouseAdapter
	{
		@Override//��갴��
		public void mousePressed(MouseEvent e)
		{
			if(start)
			{
				drawline=true;
				t=new Thread(new extendLine());//����һ���߳�������������
				t.start();
			}
		}

		
		@Override//����ͷ�
		public void mouseReleased(MouseEvent e)
		{
			if(start)
			{
				drawline=false;
				t=new Thread(new revoleLine());
				revoleline=true;
				h=length;
				t.start();
				
			}
			else
			{
				start=true;
				end=false;
				repaint();
			}
		}
		
	}
	//��������
	class extendLine implements Runnable
	{
		public void run()
		{
			while(drawline)
			{
				length++;
				repaint();
				try 
				{
					Thread.sleep(5);
				} 
				catch (InterruptedException ex)
				{
					ex.printStackTrace();
				}
				
			}	
		}
		
	}
	//��ת����
	class revoleLine implements Runnable
	{
		public void run()
		{
			while(width<=length)
			{
				width++;
				int num=length*length-width*width;
				h=(int)Math.sqrt(num);
				repaint();
				try 
				{
					Thread.sleep(5);
				} 
				catch (InterruptedException ex)
				{
					ex.printStackTrace();
				}
			}
			revoleline=false;
			herorun=true;
			t=new Thread(new heroRun());
			t.start();
		}
		
	}
	//�����ƶ�
	class heroRun implements Runnable
	{
		public void run() 
		{
			int p=length+80;//��ǹ������ڵ�����
			int l=p;
			if(p>=nextRect[0]&&p<=nextRect[1])
			{
				l=nextRect[1];//��������յ��ڵڶ������������ڣ���С���ܵ��ڶ������εı���
			}
			//�������������ƶ�
			while(heropoint<=l)
			{
				
				heropoint++;
				repaint();
				try 
				{
					Thread.sleep(5);
				} 
				catch (InterruptedException ex)
				{
					ex.printStackTrace();
				}
			}
			//�ƶ����֮���ж���Ϸ�Ƿ�����������ݹ���
			herorun=false;
			//�ж�
			if(l==nextRect[1])//��Ϸ�ɹ�
			{
				//�ƶ�����
				t=new Thread(new moveBk());
				bk=true;
				score++;//����+1
				t.start();
			}
			else//��Ϸʧ��
			{
				start=false;
				end=true;
				//����ʧ�ܶ�̬
				t=new Thread(new gameOver());
				t.start();
			}
		}	
	}
	//�ƶ�����
	class moveBk implements Runnable
	{
		@Override
		public void run()
		{
			while(nextRect[1]>80)
			{
				nextRect[1]--;
				nextRect[0]--;
				firstRect[1]--;
				firstRect[0]--;
				heropoint--;
				repaint();
				try 
				{
					Thread.sleep(5);
				} 
				catch (InterruptedException ex)
				{
					ex.printStackTrace();
				}
			}
			bk=false;
			//�����ƶ����֮�󣬽���������
			firstRect=nextRect;
			nextRect=CreateRect.createRect();
			length=0;
			width=0;
			h=0;
			heropoint=80;
			repaint();
		}
		
	}
	//��Ϸ����
	class gameOver implements Runnable
	{

		@Override
		public void run() 
		{
			while(width>0)
			{
				width--;
				int num=length*length-width*width;
				h=(int)Math.sqrt(num);
				repaint();
				try 
				{
					Thread.sleep(3);
				} 
				catch (InterruptedException ex) 
				{
					
					ex.printStackTrace();
				}
			}
			//�������
			length=0;
			width=0;
			h=0;
			score=0;
			heropoint=80;
			drop=0;
			firstRect=new int[]{0,80};
			nextRect=CreateRect.createRect();
		}
		
	}
}

