package pl.lome.socialsym.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import pl.lome.socialsym.scenario.CustomizableScenario;


public class MainWin {
	private static Text txtSize;
	private static Text txtInfected;
	private static Text contagionMultiplier;
	private static Text contagionWeight;
	private static Text contagionMutual;
	private static Text homophilyMultiplier;
	private static Text confoundingMultiplier;
	private static Text txtSimulationlog;
	private static Text confoundingWeight;
	private static Button btnContagion;
	private static Button btnHomophily;
	private static Button btnCSV;
	private static Button btnSimulate;
	private static Button btnConfounding;
	private static Button btnGephi;
	private static ProgressBar progressBar;
	private static Label lblProgress;
	private static Display display;
	
	public static void runSimulation() {
		final CustomizableScenario scenario = new CustomizableScenario();
		scenario.networkSize = Integer.parseInt(txtSize.getText());
		scenario.infected = Integer.parseInt(txtInfected.getText());
		
		scenario.contagion = btnContagion.getSelection();
		scenario.contagionMultiplier = Integer.parseInt(contagionMultiplier.getText());
		scenario.contagionWeight = Double.parseDouble(contagionWeight.getText());
		scenario.contagionMutual = Double.parseDouble(contagionMutual.getText());
		
		scenario.homophily = btnHomophily.getSelection();
		scenario.homophilyMultiplier = Integer.parseInt(homophilyMultiplier.getText());
		
		scenario.confounding = btnConfounding.getSelection();
		scenario.confoundingMultiplier = Integer.parseInt(confoundingMultiplier.getText());
		scenario.confoundingWeight = Double.parseDouble(confoundingWeight.getText());
		
		scenario.outputGephi = btnGephi.getSelection();
		scenario.outputCSV = btnCSV.getSelection();
		scenario.outputCVSfilename = txtSimulationlog.getText();
		
		try {

			scenario.addPropertyChangeListener(new PropertyChangeListener() {
				
				@Override
				public void propertyChange(final PropertyChangeEvent arg0) {
					Display.getDefault().asyncExec(new Runnable() {
		            	public void run() {
		            		int progress = (Integer) arg0.getNewValue();
							progressBar.setSelection(progress);
							lblProgress.setText("step " + progress + " / 100");	
		                }
		            });
									
				}
			});
			
			new Thread(new Runnable() {
			      public void run() {
			    	  try {
								scenario.run();
							} catch (Exception e) {
								e.printStackTrace();
							}
			      }
			   }).start();			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		display = Display.getDefault();
		Shell shell = new Shell();
		shell.setSize(419, 516);
		shell.setText("Lome network simulator");
		
		Group grpInitialNetwork = new Group(shell, SWT.NONE);
		grpInitialNetwork.setText("Initial network");
		grpInitialNetwork.setBounds(10, 10, 378, 82);
		
		txtSize = new Text(grpInitialNetwork, SWT.BORDER);
		txtSize.setBounds(10, 47, 76, 21);
		txtSize.setText("1000");
		
		Label lblNewLabel = new Label(grpInitialNetwork, SWT.NONE);
		lblNewLabel.setBounds(10, 26, 57, 15);
		lblNewLabel.setText("Size:");
		
		txtInfected = new Text(grpInitialNetwork, SWT.BORDER);
		txtInfected.setBounds(110, 47, 76, 21);
		txtInfected.setText("100");
		
		Label lblNewlabel = new Label(grpInitialNetwork, SWT.NONE);
		lblNewlabel.setBounds(111, 26, 76, 15);
		lblNewlabel.setText("Infected:");
		
		Group grpSimulationSteps = new Group(shell, SWT.NONE);
		grpSimulationSteps.setText("Simulation");
		grpSimulationSteps.setBounds(10, 98, 378, 232);
		
		btnContagion = new Button(grpSimulationSteps, SWT.CHECK);
		btnContagion.setBounds(10, 26, 93, 16);
		btnContagion.setText("Contagion");
		
		btnHomophily = new Button(grpSimulationSteps, SWT.CHECK);
		btnHomophily.setSelection(true);
		btnHomophily.setBounds(10, 98, 93, 16);
		btnHomophily.setText("Homophily");
		
		Label lblMultiplier = new Label(grpSimulationSteps, SWT.NONE);
		lblMultiplier.setBounds(109, 27, 55, 15);
		lblMultiplier.setText("Multiplier:");
		
		contagionMultiplier = new Text(grpSimulationSteps, SWT.BORDER);
		contagionMultiplier.setText("1000");
		contagionMultiplier.setBounds(109, 48, 76, 21);
		
		Label lblWeight = new Label(grpSimulationSteps, SWT.NONE);
		lblWeight.setBounds(191, 26, 55, 15);
		lblWeight.setText("Weight:");
		
		contagionWeight = new Text(grpSimulationSteps, SWT.BORDER);
		contagionWeight.setText("0.1");
		contagionWeight.setBounds(191, 48, 76, 21);
		
		contagionMutual = new Text(grpSimulationSteps, SWT.BORDER);
		contagionMutual.setText("0.1");
		contagionMutual.setBounds(273, 48, 76, 21);
		
		Label lblMutual = new Label(grpSimulationSteps, SWT.NONE);
		lblMutual.setText("Mutual:");
		lblMutual.setBounds(273, 26, 55, 15);
		
		homophilyMultiplier = new Text(grpSimulationSteps, SWT.BORDER);
		homophilyMultiplier.setText("5");
		homophilyMultiplier.setBounds(109, 119, 76, 21);
		
		Label label = new Label(grpSimulationSteps, SWT.NONE);
		label.setText("Multiplier:");
		label.setBounds(109, 98, 55, 15);
		
		btnConfounding = new Button(grpSimulationSteps, SWT.CHECK);
		btnConfounding.setText("Confounding");
		btnConfounding.setBounds(10, 168, 93, 16);
		
		confoundingMultiplier = new Text(grpSimulationSteps, SWT.BORDER);
		confoundingMultiplier.setText("100");
		confoundingMultiplier.setBounds(109, 189, 76, 21);
		
		Label label_1 = new Label(grpSimulationSteps, SWT.NONE);
		label_1.setText("Multiplier:");
		label_1.setBounds(109, 168, 55, 15);
		
		confoundingWeight = new Text(grpSimulationSteps, SWT.BORDER);
		confoundingWeight.setText("0.2");
		confoundingWeight.setBounds(191, 189, 76, 21);
		
		Label label_2 = new Label(grpSimulationSteps, SWT.NONE);
		label_2.setText("Weight:");
		label_2.setBounds(191, 168, 55, 15);
		
		btnSimulate = new Button(shell, SWT.NONE);
		btnSimulate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				runSimulation();
			}
		});
		btnSimulate.setBounds(10, 435, 141, 25);
		btnSimulate.setText("Run simulation");
		
		progressBar = new ProgressBar(shell, SWT.NONE);
		progressBar.setBounds(218, 435, 170, 17);
		
		Group grpOutput = new Group(shell, SWT.NONE);
		grpOutput.setText("Output");
		grpOutput.setBounds(10, 336, 378, 93);
		
		btnGephi = new Button(grpOutput, SWT.CHECK);
		btnGephi.setBounds(10, 26, 118, 16);
		btnGephi.setText("Stream to Gephi");
		
		btnCSV = new Button(grpOutput, SWT.CHECK);
		btnCSV.setSelection(true);
		btnCSV.setText("CSV");
		btnCSV.setBounds(10, 56, 87, 16);
		
		txtSimulationlog = new Text(grpOutput, SWT.BORDER);
		txtSimulationlog.setText("simulation.csv");
		txtSimulationlog.setBounds(109, 54, 126, 21);
		
		lblProgress = new Label(shell, SWT.NONE);
		lblProgress.setAlignment(SWT.CENTER);
		lblProgress.setBounds(218, 453, 170, 15);
		lblProgress.setText("step 0 / 100");

		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
