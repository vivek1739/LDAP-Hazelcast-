package com.example.ldap;

 class Dbsingleton {
	
	private static Dbsingleton instance=null;
	
	private Dbsingleton(){
	}
	
	public static Dbsingleton getInstance()
	{
	
		if(instance==null){
			instance=new Dbsingleton();
		}
		return instance;
	}

}

class Demo extends Thread
{
	public void run()
	{
		Dbsingleton instance=Dbsingleton.getInstance();
		System.out.println("for thread "+this.getName());
		System.out.println(instance);
		System.out.println("-----------");
	}
	public static void main(String[] args) throws InterruptedException {
		
		Demo[] d=new Demo[10];
		
		for(int i=0;i<10;i++)
		{
			d[i]=new Demo();
			d[i].setName("Thread "+i);
			d[i].start();
			//Demo.sleep(1000);
		}
		
		
	}
}
