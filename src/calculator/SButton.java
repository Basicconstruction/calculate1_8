package calculator;
import javax.swing.*;
import java.awt.*;

public class SButton extends JButton{
	public String signalLabel;
	public Color color = Color.BLACK;
	public SButton(String label){
		super();
		this.signalLabel = label;
	}
	public SButton(){
		super();
		this.signalLabel = "";
	}
	@Override
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(color);
		int width = this.getWidth();
		int height = this.getHeight();
		g2.setFont(new Font("微软雅黑",Font.PLAIN,16));
		int size = g2.getFont().getSize();
		g2.drawString(signalLabel,calculateX(calLength(signalLabel),size,width),calculateY(size,height));
	}
	@Override
	public void setText(String s){
		super.setText(s);
		this.signalLabel = s;
		repaint();
	}
	public int calculateX(int length,int size,int width){
		int totalX = length*size;
		return (width/2-totalX/2);
	}
	public int calculateY(int size,int height){
		return height/2+size/2;
	}
	public int calLength(String str){
		int length = 0;
		for(char c:str.toCharArray()){
			if(isAscii(c)){
				length++;
			}else{
				length+=2;
			}
		}
		return length;
	}
	public boolean isAscii(char c){
		return c<256;
	}
}
