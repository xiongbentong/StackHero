package chinen.young.main;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;

import chinen.young.panel.HeroPanel;

public class MFrame extends JFrame
{
	//������Ϸ���
	private HeroPanel heropanel;
	public MFrame()
	{
		heropanel = new HeroPanel();//������Ϸ���
		
		add(heropanel);//������
		
		setTitle("���Ӣ��");//���ñ���
		
		setSize(432,674);//���ô�С
		
		setLocationRelativeTo(null);//����λ��(����)
		
		setResizable(false);//���ò�����ı䴰���С
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//���ùرմ���ʱ�˳�����
		
		//����ϵͳͼ��
		Toolkit tk=Toolkit.getDefaultToolkit();
		Image img=tk.createImage("res/Icon.jpg");
		setIconImage(img);
		
		setVisible(true);//������ʾ����
	}
	public static void main(String[] args) 
	{
		new MFrame();
	}

}
