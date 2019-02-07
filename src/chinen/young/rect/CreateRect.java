package chinen.young.rect;

import java.util.Random;

public class CreateRect 
{
	public static int[] createRect()
	{
		Random r = new Random();
		int a[]=new int[2];
		a[0] = r.nextInt(200)+100;
		a[1] = r.nextInt(200)+100;
		if(a[0]>a[1])
		{
			int temp=a[0];
			a[0]=a[1];
			a[1]=temp;
		}
		return a;
	}
}
