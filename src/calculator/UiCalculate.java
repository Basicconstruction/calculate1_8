package calculator;

import parser.DoubleParser;
import parser.DoublePrecision;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

public class UiCalculate extends JFrame{
	public static int precision = 2;
	private JPanel showPanel;
	private JPanel buttonPanel;
	private JPanel fourLineGirdPanel;
	private JPanel twoUnRegularPanel;
	private int regularPadding = 10;
	private int oriwidth = 520;
	private int width = oriwidth+16;
	private int height = 590;
	private int showPanelHeight = 200;
	private int padding = 5;
	private StringBuilder saString = new StringBuilder();
	private StringBuilder pcString = new StringBuilder();
	private boolean lastOperationIsGetResult = false;
	private JPanel pc;//process
	private JPanel sa;//temporary and answer
	private JLabel pcLabel = new JLabel("",JLabel.RIGHT);
	private JLabel saLabel = new JLabel("",JLabel.RIGHT);
	private ArrayList<SButton> buttonGroup = new ArrayList<>(32);//>28,预设空间
	public UiCalculate(){
		super("计算器");
		this.setResizable(false);
		setLayout(null);
		setBounds(400,150,width,height);
		setPreferredSize(new Dimension(width,height));
		showPanel = new JPanel(null);
		int showPanelLeft = regularPadding;
		int showPanelAbove = showPanelLeft;
		showPanel.setBounds(showPanelLeft,showPanelAbove,oriwidth-2*showPanelLeft,showPanelHeight);
		showPanel.setBackground(new Color(0xab,0xcd,0xef));
		buttonPanel = new JPanel();
		buttonPanel.setLocation(new Point(0,showPanelHeight+2*regularPadding));
		buttonPanel.setSize(oriwidth,height-showPanelHeight-20);
		buttonPanel.setLayout(null);
		this.setMenu();
		this.addButtons();
		this.getContentPane().add(showPanel);
		this.getContentPane().add(buttonPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.enrichShowPanel();
		this.ListenerRevolver();
		//debug();
	}
	public void setMenu(){
		JMenuBar menu = new JMenuBar();
		setJMenuBar(menu);
		JMenu mi = new JMenu("模式");
		JMenuItem i11 = new JMenuItem("标准模式");
		JMenuItem i12 = new JMenuItem("高级模式");
		mi.add(i11);
		mi.add(i12);
		JMenu sets = new JMenu("选项");
		JMenu i21 = new JMenu("preference");
		JMenuItem i211 = new JMenuItem("精度");
		i21.add(i211);
		sets.add(i21);
		menu.add(mi);
		menu.add(sets);
		i211.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialog dialog = new JDialog(UiCalculate.this);
				dialog.setTitle("设置精度");
				JPanel jPrecision = new JPanel(null);
				dialog.setLocation(700,200);
				int dialogOriHeight = 200;
				dialog.setSize(300,dialogOriHeight+42);
				dialog.setContentPane(jPrecision);
				jPrecision.setSize(dialog.getWidth(),dialogOriHeight);
				int labelWidth = 100;
				int labelHeight = 30;
				int inputWidth = 40;
				int okWidth = 50;
				int okHeight = 30;
				JLabel precision = new JLabel("精度",JLabel.CENTER);
				precision.setBounds(0,(jPrecision.getHeight()-labelHeight)/2,labelWidth,labelHeight);
				JTextField inputPrecision = new JTextField();
				inputPrecision.setBounds(precision.getWidth(),(jPrecision.getHeight()-labelHeight)/2,inputWidth,labelHeight);
				JButton ok = new JButton("ok");
				ok.setBounds(jPrecision.getWidth()-3*okWidth,jPrecision.getHeight()-okHeight,okWidth,okHeight);
				jPrecision.add(precision);
				jPrecision.add(inputPrecision);
				jPrecision.add(ok);
				inputPrecision.setText(""+UiCalculate.precision);
				ok.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						UiCalculate.precision = Integer.parseInt(inputPrecision.getText());
						dialog.setVisible(false);
					//可以添加持久化设置选项
					}
				});
				dialog.setVisible(true);
			}
		});

	}
	public void addButtons(){
		this.fourLineGirdPanel = new JPanel();
		this.twoUnRegularPanel = new JPanel(null);
		JPanel ref = this.fourLineGirdPanel;
		JPanel ret = this.twoUnRegularPanel;
		this.buttonPanel.add(ref);
		this.buttonPanel.add(ret);
		ref.setBounds(regularPadding,0,oriwidth-2*regularPadding,200);
//		String[] buttonModel = new String[]{"MC","MR","MS","M+","M-",
//				"<-","CE","C","abs","sqrt",
//				"7","8","9","/","%",
//				"4","5","6","*","1/x"
//				};
		String[] buttonModel = new String[]{"MC","MR","MS","CA","BACK",
				"<-","(",")","abs","sqrt",
				"7","8","9","/","%",
				"4","5","6","*","1/x"
		};
		ref.setLayout(new GridLayout(4,4,5,5));
		for(String s:buttonModel){
			SButton i = new SButton(s);
			ref.add(i);
			buttonGroup.add(i);
		}
		String[] buttonModel2 = new String[]{
				"1","2","3","-","0",".","+","="
		};
		int baseWidth = (oriwidth-2*regularPadding-4*5)/5;
		int baseHeight = (200-5*3)/4;
		JPanel interJPanel = new JPanel(null);
		ret.add(interJPanel);
		ret.setBounds(regularPadding,200+padding,oriwidth-2*regularPadding,100);
		SButton[] sbt = new SButton[8];
		for(int i = 0;i < 8;i++){
			SButton j = new SButton(buttonModel2[i]);
			sbt[i] = j;
			ret.add(j);
			buttonGroup.add(j);
		}
		sbt[0].setBounds(0,0,baseWidth,baseHeight);
		sbt[1].setBounds(revolveWidth(1,baseWidth),0,baseWidth,baseHeight);
		sbt[2].setBounds(revolveWidth(2,baseWidth),0,baseWidth,baseHeight);
		sbt[3].setBounds(revolveWidth(3,baseWidth),0,baseWidth,baseHeight);
		sbt[4].setBounds(0,baseHeight+padding,baseWidth*2+padding,baseHeight);
		sbt[5].setBounds(revolveWidth(2,baseWidth),baseHeight+padding,baseWidth,baseHeight);
		sbt[6].setBounds(revolveWidth(3,baseWidth),baseHeight+padding,baseWidth,baseHeight);
		sbt[7].setBounds(revolveWidth(4,baseWidth),0,baseWidth,baseHeight*2+padding);

	}
	private int revolveWidth(int n,int w){
		return (w+padding)*n;
	}
	public void debug(){
		this.showPanel.setBackground(Color.RED);
		this.buttonPanel.setBackground(Color.BLUE);
		this.fourLineGirdPanel.setBackground(Color.BLACK);
	}
	private void enrichShowPanel(){
		this.pc = new JPanel(null);
		this.sa = new JPanel(null);
		float gene = 0.4f;
		this.pc.setBounds(0,0,showPanel.getWidth(), (int) (showPanel.getHeight()*gene));
		this.sa.setBounds(0,this.pc.getHeight(),showPanel.getWidth(), (int) (showPanel.getHeight()*(1-gene)));
//		this.pc.setBackground(Color.RED);
//		this.sa.setBackground(Color.PINK);
		showPanel.add(this.pc);
		showPanel.add(this.sa);
		this.pc.add(pcLabel);
		this.sa.add(saLabel);
		pcLabel.setBounds(0,0,pc.getWidth(),pc.getHeight());
		saLabel.setBounds(0,0,sa.getWidth(),sa.getHeight());
		pcLabel.setFont(new Font("微软雅黑",Font.PLAIN,14));
		saLabel.setFont(new Font("微软雅黑",Font.PLAIN,24));
//		pcLabel.setText("test");
//		saLabel.setText("test");
	}
	private void ListenerRevolver(){
		String[] nums = new String[]{
				"0","1","2","3","4","5","6","7","8","9","."
		};
		String[] operators = new String[]{
				"+","-","*","/","%"
		};
		for(SButton sbt:this.buttonGroup){
			if(findFirstOf(sbt.signalLabel,nums)!=-1){
				sbt.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						if(lastOperationIsGetResult){
							pcString = new StringBuilder();
							pcLabel.setText("");
							saString = new StringBuilder();
						}
						saString.append(sbt.signalLabel);
						saLabel.setText(saString.toString());
						lastOperationIsGetResult = false;
					}
				});
			}else if(findFirstOf(sbt.signalLabel,operators)!=-1){
				sbt.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						if(lastOperationIsGetResult){
							pcString = new StringBuilder();
							pcLabel.setText(pcString.toString());

						}
						saLabel.setText("");
						pcString.append(saString).append(sbt.signalLabel);
						pcLabel.setText(pcString.toString());
						saString = new StringBuilder();
						lastOperationIsGetResult = false;
					}
				});
			}else if(sbt.signalLabel.equals("=")){
        		sbt.addActionListener(new ActionListener() {
              	@Override
			  	public void actionPerformed(ActionEvent e) {
              		pcString.append(saString.toString());
                  	DoubleParser parser = new DoubleParser(pcString.toString());
                  	saString = new StringBuilder(""+DoublePrecision.round(parser.getDoubleResult(),precision));
                  	saLabel.setText(saString.toString());
                  	pcString.append("=");
					pcLabel.setText(pcString.toString());
					lastOperationIsGetResult = true;
              	}
        		});
			}else if(sbt.signalLabel.equals("1/x")){
				sbt.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						if(lastOperationIsGetResult){
							pcString = new StringBuilder();
							pcLabel.setText(pcString.toString());

						}
						if(saString.toString().contains(".")){
							double t = Double.parseDouble(saString.toString());
							t = 1/t;
							saString = new StringBuilder(""+t);
						}else{
							int t = Integer.parseInt(saString.toString());
							double tp = 1.0/t;
							saString = new StringBuilder(""+tp);
						}
						saLabel.setText(saString.toString());
						lastOperationIsGetResult = false;
					}
				});
			}else if(sbt.signalLabel.equals("CA")){
				sbt.addActionListener(e -> {
					saString = new StringBuilder();
					pcString = new StringBuilder();
					saLabel.setText("");
					pcLabel.setText("");
					lastOperationIsGetResult = false;
				});
			}else if(sbt.signalLabel.equals("(")||sbt.signalLabel.equals(")")){
				sbt.addActionListener(e -> {
					if(saString.toString().length()!=0){
						if(sbt.signalLabel.equals("(")){
							pcString.append(saString).append("*").append(sbt.signalLabel);
						}else{
							pcString.append(saString).append(sbt.signalLabel);
						}
						saString = new StringBuilder();
						saSync();
					}else{
						pcString.append(sbt.signalLabel);
					}
					pcSync();
				});
			}else{
				sbt.addActionListener(e->{
					JDialog dialog = new JDialog(UiCalculate.this);
					dialog.setTitle("surprise!");
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					Random r = new Random();
					dialog.setBounds(r.nextInt(2000),r.nextInt(1300),300,300);
					JPanel cookie = new JPanel(new BorderLayout());
					dialog.setContentPane(cookie);
					JLabel cook = new JLabel("你找到了一个没有被实现的按钮！\n" +
							"你可以尝试去实现一下！",JLabel.CENTER);
					cook.setSize(300,300);
					dialog.getContentPane().add(BorderLayout.CENTER,cook);
					Color color = new Color(r.nextInt(255),r.nextInt(255),r.nextInt(255));
					cook.setOpaque(true);
					cook.setBackground(color);
					dialog.setVisible(true);
				});
			}

		}
	}
	private void pcSync(){
		pcLabel.setText(pcString.toString());
	}
	private void saSync(){
		saLabel.setText(saString.toString());
	}
	private int findFirstOf(String s,String[] strs){
		int index = -1;
		for(int i = 0;i < strs.length;i++){
			if(s.equals(strs[i])){
				return i;
			}
		}
		return index;
	}


}

