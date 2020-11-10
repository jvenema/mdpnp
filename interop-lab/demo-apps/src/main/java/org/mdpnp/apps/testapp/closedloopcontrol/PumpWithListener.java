package org.mdpnp.apps.testapp.closedloopcontrol;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.mdpnp.apps.fxbeans.NumericFx;
import org.mdpnp.apps.fxbeans.NumericFxList;
import org.mdpnp.apps.testapp.Device;
import org.mdpnp.guis.waveform.javafx.JavaFXWaveformPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rti.dds.infrastructure.InstanceHandle_t;

import ice.FlowRateObjectiveDataWriter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class PumpWithListener {
	
	@FXML Label pumpId;
	@FXML TextField requestedSpeed;
	@FXML Button setSpeedButton;
	@FXML Label currentSpeedLabel;
	
	private Device pump;
	private FlowRateObjectiveDataWriter writer;
	private Connection dbconn;
	private PreparedStatement controlStatement;
	
	private static final Logger log = LoggerFactory.getLogger(PumpWithListener.class);
	private final String FLOW_RATE=rosetta.MDC_FLOW_FLUID_PUMP.VALUE;
	
	
	public void initialize() {
		
	}
	
	class NumericValueChangeListener implements ChangeListener<Number> {

		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			// TODO Auto-generated method stub
			System.err.println("pumpSpeedListener newValue is "+newValue.floatValue());
			currentSpeedLabel.setText(Float.toString(newValue.floatValue()));
		}
		
	}
	
	NumericValueChangeListener pumpSpeedListener=new NumericValueChangeListener();
	
	public void setPump(Device pump, NumericFxList list, FlowRateObjectiveDataWriter writer, Connection dbconn) {
		this.pump=pump;
		this.writer=writer;
		this.dbconn=dbconn;
		
		pumpId.textProperty().bind(pump.comPortProperty());
		
		list.forEach( n -> {
			log.info("handleDeviceChange numeric dev ident is "+n.getUnique_device_identifier()+" "+n.getMetric_id());
			if( ! n.getUnique_device_identifier().equals(pump.getUDI())) return;	//Some other device
			//When we get here, we are looking at a property for the currently selected device
			if(n.getMetric_id().equals(FLOW_RATE)) {
				currentSpeedLabel.setText(Integer.toString((int)n.getValue()));
				n.valueProperty().addListener(pumpSpeedListener);
				System.err.println("Added pump speed listener to "+pump.getComPort());
			}
		});

	}
}
