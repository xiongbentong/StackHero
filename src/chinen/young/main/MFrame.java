package chinen.young.main;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;

import chinen.young.panel.HeroPanel;

public class MFrame extends JFrame
{
	//定义游戏面板
	private HeroPanel heropanel;
	public MFrame()
	{
		heropanel = new HeroPanel();//创建游戏面板
		
		add(heropanel);//添加面板
		
		setTitle("光棍英雄");//设置标题
		
		setSize(432,674);//设置大小
		
		setLocationRelativeTo(null);//设置位置(居中)
		
		setResizable(false);//设置不允许改变窗体大小
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置关闭窗口时退出程序
		
		//更改系统图标
		Toolkit tk=Toolkit.getDefaultToolkit();
		Image img=tk.createImage("res/Icon.jpg");
		setIconImage(img);
		
		setVisible(true);//设置显示界面
	}
	public static void main(String[] args) 
	{
		new MFrame();
	}

}
