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
	public static int precision = 2;//精度设置静态变量
	private JPanel showPanel;//展示区域总面板
	private JPanel buttonPanel;// Button按钮的总面板
	private JPanel fourLineGirdPanel;//Button按钮的前四行，大小一致的按钮的面板。
	private JPanel twoUnRegularPanel;//Button按钮的后两行，大小不一致的按钮的面板。
	private int regularPadding = 10;//常用的面板与界面的间距
	private int oriwidth = 520;//设计application的宽度
	private int width = oriwidth+16;//修正application宽度
	private int height = 590;//设计application的高度
	private int showPanelHeight = 200;//设计展示区域面板的高度
	private int padding = 5;//按钮之间的间距
	private StringBuilder saString = new StringBuilder();//临时数字输入，结果展示面板的处理字符串变量
	private StringBuilder pcString = new StringBuilder();//表达式展示面板的处理字符串变量
	private boolean lastOperationIsGetResult = false;//状态，用于确定上一次的操作是不是=
	private JPanel pc;//临时数字输入，结果展示面板
	private JPanel sa;//临时数字输入，结果展示面板
	private JLabel pcLabel = new JLabel("",JLabel.RIGHT);///表达式展示面板的展示Label
	private JLabel saLabel = new JLabel("",JLabel.RIGHT);//临时数字输入，结果展示面板的Label
	private ArrayList<SButton> buttonGroup = new ArrayList<>(32);//>28,预设空间，存储所有按钮引用的‘数组’
	private static String ans = "";//存储上一次结果的字符串变量
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
	/**
	 * 为应用程序添加菜单栏
	 * **/
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
	/**
	 * 为应用程序添加按钮
	 * **/
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
				"<-","(",")","ans","sqrt",
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
	/**
	 * 用于addButtons()，简化计算偏移位置
	 * **/
	private int revolveWidth(int n,int w){
		return (w+padding)*n;
	}
	/**
	 * 用于处理绝对位置的debug方法
	 * **/
	public void debug(){
		this.showPanel.setBackground(Color.RED);
		this.buttonPanel.setBackground(Color.BLUE);
		this.fourLineGirdPanel.setBackground(Color.BLACK);
	}
	/**
	 * 对展示区域面板进行细化操作
	 * **/
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
	/**
	 * 为添加的按钮添加监听器，注意这个函数实际上只会调用一次，即添加监听器这个过程
	 * 不会造成严重的运行开销
	 * **/
	private void ListenerRevolver(){
		String[] nums = new String[]{
				"0","1","2","3","4","5","6","7","8","9","."
		};
		String[] operators = new String[]{
				"+","-","*","/","%"
		};
		for(SButton sbt:this.buttonGroup){
			if(findFirstOf(sbt.signalLabel,nums)!=-1){
				// 0 1 2 3 4 5 6 7 8 9 . 按钮，特殊的是 . 也被归于这个按钮。
				sbt.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						standardEraseAfterEqual();
						saString.append(sbt.signalLabel);
						saSync();
						pcSync();
						standardStatusChange(sbt);
					}
				});
			}else if(findFirstOf(sbt.signalLabel,operators)!=-1){
				// + - * / 按钮
				sbt.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						if(lastOperationIsGetResult){
							pcString = new StringBuilder();
							pcString.append(saString).append(sbt.signalLabel);
							saString = new StringBuilder();
						}else{
							pcString.append(saString).append(sbt.signalLabel);
							saString = new StringBuilder();
						}
						saSync();
						pcSync();
						standardStatusChange(sbt);
					}
				});
			}else if(sbt.signalLabel.equals("=")){
				// = 按钮，用于计算结果。会先将saLabel上的值汇集到pcLabel上
        		sbt.addActionListener(new ActionListener() {
              	@Override
			  	public void actionPerformed(ActionEvent e) {
              		pcString.append(saString.toString());
                  	DoubleParser parser = new DoubleParser(UiUtils.ansSerialize(pcString.toString(),ans));
                  	saString = new StringBuilder(""+DoublePrecision.round(parser.getDoubleResult(),precision));
					ans = saString.toString();
                  	pcString.append("=");
					saSync();
					pcSync();
					standardStatusChange(sbt);
              	}
        		});
			}else if(sbt.signalLabel.equals("1/x")){
				// 直接计算当前 saLabel上的saString代表的值的倒数，并作为新的saString
				sbt.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						if(saString.toString().contains(".")){
							double t = Double.parseDouble(saString.toString());
							t = 1/t;
							saString = new StringBuilder(""+t);
						}else{
							int t = Integer.parseInt(saString.toString());
							double tp = 1.0/t;
							saString = new StringBuilder(""+tp);
						}
						pcString = new StringBuilder();
						pcSync();
						saSync();
						standardStatusChange(sbt);
					}
				});
			}else if(sbt.signalLabel.equals("CA")){
				// as clear all,清空按钮，不会清空ans，当然也可以设置为清空ans
				sbt.addActionListener(e -> {
					clearAll();
					saSync();
					pcSync();
					standardStatusChange(sbt);
				});
			}else if(sbt.signalLabel.equals("ans")){
				//   ans 按钮
				sbt.addActionListener(e -> {
					standardEraseAfterEqual();
					pcString.append("ans");
					pcSync();
					saSync();
					standardStatusChange(sbt);
				});
			}else if(sbt.signalLabel.equals("(")||sbt.signalLabel.equals(")")){
				//     (  和  )   按钮
				sbt.addActionListener(e -> {
					if(lastOperationIsGetResult){
						clearAll();
						pcString.append(sbt.signalLabel);
					}else{
						if(saString.toString().length()!=0){
							if(sbt.signalLabel.equals("(")){
								pcString.append(saString).append("*").append(sbt.signalLabel);
							}else{
								pcString.append(saString).append(sbt.signalLabel);
							}
							saString = new StringBuilder();
						}else{
							pcString.append(sbt.signalLabel);
						}
					}
					saSync();
					pcSync();
					standardStatusChange(sbt);
				});
			}else if(sbt.signalLabel.equals("BACK")){
				//回退按钮
				sbt.addActionListener(e->{
					if(!saString.isEmpty()){
						saPop();
						if(lastOperationIsGetResult){
							pcString = new StringBuilder();
							pcSync();
						}
					}else{
						if(!pcString.isEmpty()){
							pcPop();
						}
					}
					standardStatusChange(sbt);
				});
			}else{
				//未定义的按钮
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
					standardStatusChange(sbt);
				});
			}

		}
	}
	/**
	 * 通用的状态转变改变函数
	 * **/
	private void standardStatusChange(SButton sbt){
		lastOperationIsGetResult = sbt.signalLabel.equals("=");
	}
	/**
	 * 自动的根据状态初始化字符串持有对象。
	 * **/
	private void standardEraseAfterEqual(){
		if(lastOperationIsGetResult){
			pcString = new StringBuilder();
			saString = new StringBuilder();
		}
	}
	private void clearAll(){
		pcString = new StringBuilder();
		saString = new StringBuilder();
	}
	/**
	 * 用于根据pcString更新pcLabel的text
	 * **/
	private void pcSync(){
		pcLabel.setText(pcString.toString());
	}
	/**
	 * 用于根据saString更新saLabel的text
	 * **/
	private void saSync(){
		saLabel.setText(saString.toString());
	}
	/**
	 * 未经优化的查找算法，从 String[] strs 查找 String s,如果存在，返回索引值，否则，返回-1
	 * **/
	private int findFirstOf(String s,String[] strs){
		int index = -1;
		for(int i = 0;i < strs.length;i++){
			if(s.equals(strs[i])){
				return i;
			}
		}
		return index;
	}
	/**
	 * 用于为BACK按钮添加回退方法，这是其中的一个依赖函数
	 * **/
	private void pcPop(){
		StringBuilder numberString = new StringBuilder();
		Character c;
		while((c=popOneChar(pcString))!=null&&UiUtils.isDigitChar(c)){
			numberString.insert(0,c);
		}
    	if (numberString.isEmpty()){
			saString = new StringBuilder();
		}else{
    		saString = new StringBuilder().append(numberString);
		}
		saSync();
    	pcSync();
	}
	/**
	 * 用于为BACK按钮添加回退方法，这是其中的一个依赖函数
	 * **/
	private void saPop(){
		if(!saString.isEmpty()){
			popOneChar(saString);
			saSync();
		}
	}
	/**
	 * 用于为BACK按钮添加回退方法，这是 pcPop()和saPop()的依赖函数。
	 * **/
	private Character popOneChar(StringBuilder sbr){
		//不可扩展的背后pop
		int len = sbr.length();
		Character c;
		if(len!=0){
			c = sbr.toString().charAt(len-1);
			sbr.delete(len-1,len);
		}else{
			c = null;
		}
		return c;
	}


}

