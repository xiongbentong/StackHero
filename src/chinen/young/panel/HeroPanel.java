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
	//定义背景图片
	private BufferedImage background;//背景图片
	private BufferedImage hero;//人物图片
	private boolean start;//开始
	private boolean end;//结束
	private boolean drawline;//判断是否在画线状态
	private boolean revoleline;//判断是否在旋转线状态
	private boolean herorun;//判断人物是否移动
	private boolean bk;//判断背景是否移动
	private static final int height=474;//始终在此线上
	private int length;//标记画线的长度
	private int width;//标记线旋转时的x坐标
	private int h;//标记线旋转时的y坐标
	private int heropoint;//人物位置
	private int drop;//人物下降
	private int score;//分数
	private int herowidth;//用于标记人物大小
	private int heroheight;
	private int[] firstRect;//游戏界面上的第一个矩形
	private int[] nextRect;//游戏界面上的第二个矩形
	private Thread t;//线程变量 控制游戏
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
		
		g.drawImage(background, 0, 0, null);//画背景图
		Font font=new Font("楷体",Font.BOLD,30);
		g.setFont(font);
		
		if(!start&&!end)//游戏还没有开始
		{
			//画开始界面
			g.drawString("Strat Game", 140,300);
		}
		
		if(start&& !bk)//游戏开始了但是没有移动背景
		{
			g.fillRect(firstRect[0], height, firstRect[1]-firstRect[0], 200);
			g.fillRect(nextRect[0], height, nextRect[1]-nextRect[0], 200);
			g.drawString("Score:"+score, 150, 70);
		}
		
		if(!herorun&&start)//初始化人物位置保持不变
		{
			g.drawImage(hero, heropoint-herowidth, height-heroheight, null);
		}
		
		if(drawline)//画线(增长)
		{
			g.drawLine(80, height, 80, height-length);
		}
		
		if(revoleline)//画线(旋转)
		{
			g.drawLine(80, height, 80+width, height-h);
		}
		
		if(herorun)//人物移动
		{
			g.drawLine(80, height, 80+length, height);
			g.drawImage(hero, heropoint-herowidth, height-heroheight, null);
		}
		
		if(bk)//背景移动（包括矩形 线条 分数）
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
			g.drawString("分数:"+score, 150, 70);
			g.drawString("Game Over", 140, 300);
			g.drawImage(hero, heropoint-herowidth, height-heroheight+20*drop++, null);
			g.drawLine(80, height, 80+width, height+h);
		}
		
	}
	//鼠标监听
	class MyMouseListener extends MouseAdapter
	{
		@Override//鼠标按下
		public void mousePressed(MouseEvent e)
		{
			if(start)
			{
				drawline=true;
				t=new Thread(new extendLine());//创建一个线程用于增长棍子
				t.start();
			}
		}

		
		@Override//鼠标释放
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
	//增长棍子
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
	//旋转棍子
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
	//人物移动
	class heroRun implements Runnable
	{
		public void run() 
		{
			int p=length+80;//标记棍子所在的坐标
			int l=p;
			if(p>=nextRect[0]&&p<=nextRect[1])
			{
				l=nextRect[1];//如果棍子终点在第二个矩形区域内，则小人跑到第二个矩形的边上
			}
			//根据条件进行移动
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
			//移动完成之后判断游戏是否结束并将数据归零
			herorun=false;
			//判断
			if(l==nextRect[1])//游戏成功
			{
				//移动背景
				t=new Thread(new moveBk());
				bk=true;
				score++;//分数+1
				t.start();
			}
			else//游戏失败
			{
				start=false;
				end=true;
				//开启失败动态
				t=new Thread(new gameOver());
				t.start();
			}
		}	
	}
	//移动背景
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
			//背景移动完成之后，将数据重置
			firstRect=nextRect;
			nextRect=CreateRect.createRect();
			length=0;
			width=0;
			h=0;
			heropoint=80;
			repaint();
		}
		
	}
	//游戏结束
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
			//数据清空
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

