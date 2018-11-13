package com.android.img.crop.swing;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import com.android.img.crop.listener.GenerateListener;
import com.android.img.crop.model.ConfigItem;
import com.android.img.crop.utils.ConfigUtils;
import com.android.img.crop.utils.GenerateBuilder;

public class ImageCropMainFrame extends JFrame implements GenerateListener {
	public JPanel jPanel;
	public JLabel jLabel, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6, jLabel7,
			jLabel8, jLabel9, jLabel10, jLabel11;
	public JButton jButton, jButton2, jButton3, jButton4, jButton5, jButton6,
			jButton7;
	public JTextField jField, jField2, jField3, jField4, jField5;
	public JComboBox jcb1;
	public JCheckBox recursion;
	public String filePath = System.getProperty("user.home");
	public JPanel panel_category_select;
	public int index = 0;
	int width = 800;
	int height = 600;
	int componetHeight = height / 20;
	int x = 0;
	int y = 0;
	public List<JCheckBox> checkBoxs = new ArrayList<JCheckBox>();
	// public String all[] = { "ldpi", "mdpi" , "hdpi","xhdpi", "xxhdpi"};
	public List<ConfigItem> models;
	public String iconName;

	public static void main(String[] args) {
		ImageCropMainFrame thisClass = new ImageCropMainFrame();
		thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		thisClass.setVisible(true);
	}

	private void loadConfig() {
		// InputStream
		// is=this.getClass().getResourceAsStream("/resource/res.txt");
		try {
			models = ConfigUtils
					.getConfigByDefault().getSizeItem();
			iconName = ConfigUtils.getConfigByDefault().getIconName();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ImageCropMainFrame() {
		super();
		this.setTitle("移动端图片裁剪工具");
		File file = new File(filePath + "/config.xml");
		if (!file.exists()) {
			loadConfig();
		} else {
			try {
				models = ConfigUtils
						.getConfigByDefault().getSizeItem();
				iconName = ConfigUtils.getConfigByDefault().getIconName();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		init();
		initListener();
		Rectangle rectangle = jPanel.getBounds();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = screenSize.width / 2;
		int centerY = screenSize.height / 2;
		setBounds(centerX - width / 2, centerY - height / 2, width, height);
		setVisible(true);
		setLocationRelativeTo(getOwner()); //居中显示
		this.setResizable(true);
		setLocationRelativeTo(null);

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (getWidth() < width) {
			setSize(width,getHeight());
		}
		if(getHeight() < height){
			setSize(getWidth(),height);
		}
		jPanel.setBounds(0,0,getWidth(),getHeight());
	}

	void init() {
		// new Color(51,153,51)绿色
		// new Color(102,153,204) //蓝色
		// new Color(204,204,204);//灰色
		JFrame.setDefaultLookAndFeelDecorated(true);
		y = 0;
		x = 0;
		checkBoxs.clear();
		setLayout(null);
		jPanel = new JPanel();
		jPanel.setLayout(null);
		jPanel.setBounds(new Rectangle(0, 0, width, height));
		jPanel.setBackground(new Color(102, 153, 204));
		jLabel = new JLabel();
		jLabel.setBounds(0, 0, width, height);

		int padding = 10;
		jLabel7 = new JLabel("文件路径：");
		jLabel7.setForeground(Color.WHITE);
		jLabel7.setFont(new Font(null, 1, 15));
		x = padding;
		y = height / 12;
		jLabel7.setBounds(x, y, (width - x) / 5, componetHeight);
		jField = new JTextField();
		jField.setText("");
		jField.setBounds(x + jLabel7.getWidth(), y, (width - x) * 3 / 5,
				componetHeight);
		jButton = new JButton("选择");
		jButton.setBounds(x + jLabel7.getWidth() + jField.getWidth(), y,
				(width - x) / 5, componetHeight);
		jField.setDragEnabled(true);
		jField.setTransferHandler(new TransferHandler() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean importData(JComponent comp, Transferable t) {
				// TODO Auto-generated method stub
				try {
					Object o = t.getTransferData(DataFlavor.javaFileListFlavor);
					// 此处输出文件/文件夹的名字以及路径
					if (o != null) {
						String path = o.toString();
						path = path.replace("[", "");
						path = path.replace("]", "");
						File file = new File(path);
						if (file.isDirectory()) {
							jField.setText(path);
						} else {
							JOptionPane.showMessageDialog(null, "请拖入文件夹", "失败",
									JOptionPane.ERROR_MESSAGE);
						}
					}
					System.out.println("sdf" + o.toString());
					return true;
				} catch (UnsupportedFlavorException ufe) {
					ufe.printStackTrace();
					return true;
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}

			@Override
			public boolean canImport(JComponent comp,
					DataFlavor[] transferFlavors) {
				// TODO Auto-generated method stub
				return true;
			}
		});

		// 输出路径
		x = padding;
		y = y + componetHeight + 20;
		jLabel10 = new JLabel("输出路径：");
		jLabel10.setForeground(Color.WHITE);
		jLabel10.setFont(new Font(null, 1, 15));
		jLabel10.setBounds(x, y, (width - x) / 5, componetHeight);
		jField5 = new JTextField();
		jField5.setText("");
		jField5.setBounds(x + jLabel7.getWidth(), y, (width - x) * 3 / 5,
				componetHeight);
		jButton7 = new JButton("选择");
		jButton7.setBounds(x + jLabel7.getWidth() + jField.getWidth(), y,
				(width - x) / 5, componetHeight);
		jField5.setDragEnabled(true);
		jField5.setTransferHandler(new TransferHandler() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean importData(JComponent comp, Transferable t) {
				// TODO Auto-generated method stub
				try {
					Object o = t.getTransferData(DataFlavor.javaFileListFlavor);
					// 此处输出文件/文件夹的名字以及路径
					if (o != null) {
						String path = o.toString();
						path = path.replace("[", "");
						path = path.replace("]", "");
						File file = new File(path);
						if (file.isDirectory()) {
							jField5.setText(path);
						} else {
							JOptionPane.showMessageDialog(null, "请拖入文件夹", "失败",
									JOptionPane.ERROR_MESSAGE);
						}
					}
					System.out.println("sdf" + o.toString());
					return true;
				} catch (UnsupportedFlavorException ufe) {
					ufe.printStackTrace();
					return true;
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}

			@Override
			public boolean canImport(JComponent comp,
					DataFlavor[] transferFlavors) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		
		x = padding;
		y = y + componetHeight + 20;
		JLabel jLabel16 = new JLabel("是否递归：");
		jLabel16.setFont(new Font(null, 1, 15));
		jLabel16.setForeground(Color.white);
		jLabel16.setBounds(x, y,  (width - x) / 5,
				componetHeight);
		
		x = x + jLabel16.getWidth();
		recursion = new JCheckBox("选择");
		recursion.setBackground(new Color(102, 153, 204));
		recursion.setBounds(x, y, (width - padding) / 6,
				componetHeight);



		x = padding;
		jLabel8 = new JLabel("设计尺寸：");
		jLabel8.setFont(new Font(null, 1, 15));
		jLabel8.setForeground(Color.white);
		y = y + componetHeight + 20;
		jLabel8.setBounds(x, y, (width - padding) / 6,
				componetHeight);
		jcb1 = new JComboBox(new DefaultComboBoxModel());
		x = x + jLabel8.getWidth();
		jcb1.setBounds(x, y, (width - padding) / 6,
				componetHeight);
		jLabel3 = new JLabel("宽度：");
		jLabel3.setFont(new Font(null, 1, 15));
		jLabel3.setForeground(Color.white);
		x = x + jcb1.getWidth();
		jLabel3.setBounds(x, y, (width - padding) / 6,
				componetHeight);
		jField2 = new JTextField();
		x = x + jLabel3.getWidth();
		jField2.setBounds(x, y, (width - padding) / 6,
				componetHeight);
		jLabel4 = new JLabel("高度：");
		jLabel4.setFont(new Font(null, 1, 15));
		jLabel4.setForeground(Color.white);
		x = x + jField2.getWidth();
		jLabel4.setBounds(x, y, (width - padding) / 6,
				componetHeight);
		jField3 = new JTextField();
		x = x + jLabel4.getWidth();
		jField3.setBounds(x, y, (width - padding) / 6,
				componetHeight);
		jLabel9 = new JLabel("图片名称：");
		jLabel9.setFont(new Font(null, 1, 15));
		jLabel9.setForeground(Color.white);
		x = padding;
		y = y + componetHeight + 20;
		jLabel9.setBounds(x, y, (width - padding) / 5,
				componetHeight);
		jField4 = new JTextField();
		x = x + jLabel9.getWidth();
		jField4.setBounds(x, y, (width - padding) * 4 / 5,
				componetHeight);
		jField4.setTransferHandler(new TransferHandler() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean importData(JComponent comp, Transferable t) {
				// TODO Auto-generated method stub
				try {
					Object o = t.getTransferData(DataFlavor.javaFileListFlavor);
					// 此处输出文件/文件夹的名字以及路径
					if (o != null) {
						String path = o.toString();
						path = path.replace("[", "");
						path = path.replace("]", "");
						File file = new File(path);
						if (file.isFile()) {
							if (file.getName().endsWith(".jpg")
									|| file.getName().endsWith(".png")
									|| file.getName().endsWith(".JPG")
									|| file.getName().endsWith(".PNG")) {
								jField4.setText(file.getName());
							} else {
								JOptionPane.showMessageDialog(null,
										"请拖入正确的图片文件，当前只支持jpg和png图片", "失败",
										JOptionPane.ERROR_MESSAGE);
							}
						} else {
							JOptionPane.showMessageDialog(null, "请拖入文件", "失败",
									JOptionPane.ERROR_MESSAGE);
						}
					}
					System.out.println("sdf" + o.toString());
					return true;
				} catch (UnsupportedFlavorException ufe) {
					ufe.printStackTrace();
					return true;
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}

			@Override
			public boolean canImport(JComponent comp,
					DataFlavor[] transferFlavors) {
				// TODO Auto-generated method stub
				return true;
			}
		});

		x = padding;
		y = y + componetHeight + 20;
		jLabel5 = new JLabel("生成尺寸：");
		jLabel5.setFont(new Font(null, 1, 15));
		jLabel5.setForeground(Color.white);
		jLabel5.setBounds(x, y, width - padding,
				componetHeight);
		x = padding;
		y = y + componetHeight + 20;
		panel_category_select = new JPanel();
		panel_category_select.setLayout(null);
		panel_category_select.setBounds(x, y,
				width - padding,
				models.size() % 4 > 0 ? (models.size() / 4 + 1)
						* (componetHeight + 20) : (models.size() / 4)
						* (componetHeight + 20));
		panel_category_select.setBackground(new Color(102, 153, 204));
		int myX = 0 - (width - padding) / 4;
		int myY = 0;
		boolean select = false;
		int count = 0;
		for (ConfigItem model : models) {
			if (count >= 4) {
				count = 0;
				myX = 0;
				myY = myY + componetHeight + 20;
			} else {
				myX = myX + (width - padding) / 4;
			}
			JCheckBox checkBox = new JCheckBox(model.getName());
			checkBox.setBackground(new Color(102, 153, 204));
			checkBox.setBounds(myX, myY,
					(width - padding) / 4, componetHeight);
			checkBoxs.add(checkBox);
			if (!select) {
				checkBox.setSelected(true);
				select = false;
			}
			panel_category_select.add(checkBox);
			count++;
		}
		
		jLabel6 = new JLabel("注：如果选择了源尺寸将会覆盖源文件");
		jLabel6.setFont(new Font(null, 1, 15));
		jLabel6.setForeground(Color.red);
		jLabel6.setFont(new Font("Dialog", 1, 12));
		x = padding;
		y = y + panel_category_select.getHeight() + 20;
		jLabel6.setBounds(x, y, width - padding,
				componetHeight);
		jButton3 = new JButton("马上生成");
		jButton3.setFont(new Font(null, 1, 15));
		jButton3.setForeground(Color.red);
		x = padding;
		y = y + jLabel6.getHeight() + 20;
		jButton3.setBounds(x, y, (width - padding) / 4,
				componetHeight);

		jButton4 = new JButton("生成配置文件");
		jButton4.setFont(new Font(null, 1, 15));
		jButton4.setForeground(Color.red);
		x = x + jButton3.getWidth();
		jButton4.setBounds(x, y, (width - padding) / 4,
				componetHeight);

		jButton5 = new JButton("加载配置");
		jButton5.setFont(new Font(null, 1, 15));
		jButton5.setForeground(Color.red);
		x = x + jButton4.getWidth();
		jButton5.setBounds(x, y, (width - padding) / 4,
				componetHeight);

		jButton6 = new JButton("清除配置");
		jButton6.setFont(new Font(null, 1, 15));
		jButton6.setForeground(Color.red);
		x = x + jButton5.getWidth();
		jButton6.setBounds(x, y, (width - padding) / 4,
				componetHeight);

		jPanel.add(jButton3);
		jPanel.add(jLabel6);
		jPanel.add(panel_category_select);
		jPanel.add(jField4);
		jPanel.add(jLabel9);
		jPanel.add(jLabel5);
		jPanel.add(jField3);
		jPanel.add(jLabel16);
		jPanel.add(recursion);
		jPanel.add(jLabel4);
		jPanel.add(jField2);
		jPanel.add(jLabel3);
		jPanel.add(jLabel8);
		jPanel.add(jcb1);
		jPanel.add(jLabel7);
		jPanel.add(jField);
		jPanel.add(jButton);
		jPanel.add(jButton4);
		jPanel.add(jButton5);
		jPanel.add(jButton6);
		jPanel.add(jLabel);
		jPanel.add(jLabel10);
		jPanel.add(jField5);
		jPanel.add(jButton7);
		add(jPanel);
		if (models != null) {
			for (int i = 0; i < models.size(); i++) {
				jcb1.addItem(models.get(i).getName());
				if (i == 0) {
					jField2.setText(models.get(i).getWidth() + "");
					jField3.setText(models.get(i).getHeight() + "");
					checkBoxs.get(0).setSelected(false);
				}
			}
		}
		jField4.setText(iconName);
	}

	private void initListener() {
		jcb1.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if (e.getStateChange() == ItemEvent.SELECTED) {
					int index = jcb1.getSelectedIndex();
					ImageCropMainFrame.this.index = index;
					String content = jcb1.getSelectedItem().toString();
					System.out.println("index222=" + index + ", content="
							+ content);
					jField2.setText(models.get(index).getWidth() + "");
					jField3.setText(models.get(index).getHeight() + "");
					jField2.setEditable(false);
					jField3.setEditable(false);
					checkBoxs.get(index).setSelected(false);
				}
			}
		});
		jButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jfc.showDialog(new JLabel(), "选择");
				File file = jfc.getSelectedFile();
				if (file != null) {
					jField.setText(file.getAbsolutePath());
				}
			}
		});
		jButton7.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jfc.showDialog(new JLabel(), "选择");
				File file = jfc.getSelectedFile();
				if (file != null) {
					jField5.setText(file.getAbsolutePath());
				}
			}
		});
		jButton3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int count = models.size();
				List<ConfigItem> useable = new ArrayList<ConfigItem>();
				for (int i = 0; i < count; i++) {
					JCheckBox checkBox = checkBoxs.get(i);
					if (checkBox.isSelected()) {
						useable.add(models.get(i));
					}
				}

				boolean isRecursion = recursion.isSelected();
				if (useable.size() > 0) {
					String path = jField.getText();
					ConfigItem currentModel = models.get(index);
					String iconName = jField4.getText();
					GenerateBuilder builder = new GenerateBuilder();
					builder.setCurrentMode(currentModel)
							.setGenerateModels(useable)
							.setRootPath(path)
							.setIconName(iconName)
							.setSaveFile(
									jField5.getText().equals("") ? null
											: new File(jField5.getText()))
							.setGenerateListener(ImageCropMainFrame.this)
							.setRecursion(isRecursion)
							.generate();
				}
			}
		});

		jButton4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				InputStream inputStream = this.getClass().getResourceAsStream(
						"/xml/default_config.xml");
				File file = new File(filePath + "/config.xml");
				FileOutputStream outputStream = null;
				try {
					if (file.exists()) {
						file.delete();
						file.createNewFile();
					}
					outputStream = new FileOutputStream(file);
					int len = 0;
					byte[] buffer = new byte[1024];
					while ((len = inputStream.read(buffer)) > -1) {
						outputStream.write(buffer, 0, len);
					}
				} catch (Exception e) {

				} finally {
					try {
						if (inputStream != null) {
							inputStream.close();
						}
						if (outputStream != null) {
							outputStream.close();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				JOptionPane.showMessageDialog(null,
						"生成成功!文件保存在" + file.getAbsolutePath(), "提示",
						JOptionPane.DEFAULT_OPTION);
			}
		});

		jButton5.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				File file = new File(filePath + "/config.xml");
				if (!file.exists()) {
					loadConfig();
				} else {
					try {
						models = ConfigUtils
								.getConfigByDefault().getSizeItem();
						iconName = ConfigUtils.getConfigByDefault().getIconName();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						panel_category_select.removeAll();
						panel_category_select.invalidate();
						getContentPane().removeAll();
						invalidate();
						init();
						initListener();
						setVisible(false);
						setVisible(true);
						JOptionPane.showMessageDialog(null, "加载完成！", "提示",
								JOptionPane.DEFAULT_OPTION);
					}
				}).start();
			}

		});
		jButton6.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				File file = new File(filePath + "/config.xml");
				if (file.exists()) {
					file.delete();
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							loadConfig();
							panel_category_select.removeAll();
							panel_category_select.invalidate();
							getContentPane().removeAll();
							invalidate();
							init();
							initListener();
							setVisible(false);
							setVisible(true);
						}
					}).start();
				}
				JOptionPane.showMessageDialog(null, "已清除", "提示",
						JOptionPane.DEFAULT_OPTION);
			}
		});
	}

	@Override
	public void generateSuccess() {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null, "生成成功!", "提示",
				JOptionPane.DEFAULT_OPTION);
		jButton3.setEnabled(true);
	}

	@Override
	public void generateFail(String message) {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null, message, "失败",
				JOptionPane.ERROR_MESSAGE);
		jButton3.setEnabled(true);
	}

	@Override
	public void generateStart() {
		// TODO Auto-generated method stub
		// JOptionPane.showMessageDialog(null, "正在生成中，请耐心等待,点击确认继续!",
		// "提示",JOptionPane.DEFAULT_OPTION);
		jButton3.setEnabled(false);
	}

	@Override
	public void generating(String filename) {
		// TODO Auto-generated method stub

	}

}
