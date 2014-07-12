package com.mdqandroid.comandocontrolador;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Set;
import java.util.TooManyListenersException;

import com.mdqandroid.Bluetooth.BluetoothSerialClient;
import com.mdqandroid.Bluetooth.BluetoothSerialClient.BluetoothStreamingHandler;
import com.mdqandroid.Bluetooth.BluetoothSerialClient.OnBluetoothEnabledListener;
import com.mdqandroid.Bluetooth.BluetoothSerialClient.OnScanListener;
import com.mdqandroid.comandocontrolador.R;
import com.mdqandroid.Comandos.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Button;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


/**
 * Blutooth 
 */
public class MainActivity extends Activity {
	
	private LinkedList<BluetoothDevice> mBluetoothDevices = new LinkedList<BluetoothDevice>();
	private ArrayAdapter<String> mDeviceArrayAdapter;
    private String R1on="001",R1off="001",R2on ="002",R2off ="002",R3on="003",R3off="003"
    		,R4on="004",R4off="004",R5on="001",R5off="002",R6on="006",R6off="006";
	private EditText mEditTextInput;
	private TextView mTextView;
	private Button mButtonSend,btn_relay1,btn_relay2,btn_relay3,btn_relay4,btn_temp;
	private ToggleButton tbtn_rele1,tbtn_rele2,tbtn_rele3,tbtn_rele4,tbtn_rele5,tbtn_rele6;
	private ProgressDialog mLoadingDialog;
	private AlertDialog mDeviceListDialog;
	private Menu mMenu;
	private BluetoothSerialClient mClient;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_main);
		 mClient = BluetoothSerialClient.getInstance();
		 
		 if(mClient == null) {
			 Toast.makeText(getApplicationContext(), "Cannot use the Bluetooth device.", Toast.LENGTH_SHORT).show();
			 finish();
		 }
		 levantarXML();
		 overflowMenuInActionBar();
		 initProgressDialog();
		 initDeviceListDialog();
		botones();
		 
	}
	
	private void levantarXML() {
		
		mTextView = (TextView) findViewById(R.id.textViewTerminal);
		mTextView.setMovementMethod(new ScrollingMovementMethod());
		mEditTextInput = (EditText) findViewById(R.id.editTextInput);
		
		btn_temp=(Button) findViewById(R.id.btn_temp);
		
		tbtn_rele1= (ToggleButton) findViewById(R.id.tBtn_Rele1);
		tbtn_rele2= (ToggleButton) findViewById(R.id.tBtn_Rele2);
		tbtn_rele3= (ToggleButton) findViewById(R.id.tBtn_Rele3);
		tbtn_rele4= (ToggleButton) findViewById(R.id.tBtn_Rele4);
		tbtn_rele5= (ToggleButton) findViewById(R.id.tBtn_Rele5);
		tbtn_rele6= (ToggleButton) findViewById(R.id.tBtn_Rele6);
		
		mButtonSend = (Button) findViewById(R.id.buttonSend);
	
	}
	private void botones() {
		
		tbtn_rele1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked){sendStringData("Rele 1 Abierto");}
				else{sendStringData("Rele 1 Cerrado");}
						}
		});
		
		tbtn_rele2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked){sendStringData("Rele 2 Abierto");}
				else{sendStringData("Rele 2 Cerrado");}
						}
		});

		tbtn_rele3.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked){sendStringData("Rele 3 Abierto");}
		else{sendStringData("Rele 3 Cerrado");}
				}
		});

		tbtn_rele4.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked){sendStringData("Rele 4 Abierto");}
		else{sendStringData("Rele 4 Cerrado");}
				}
		});

		tbtn_rele5.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked){sendStringData("Rele 5 Abierto");}
		else{sendStringData("Rele 5 Cerrado");}
				}
		});

		tbtn_rele6.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked){sendStringData("Rele 6 Abierto");}
					else{sendStringData("Rele 6 Cerrado");}
				}
		});
		
		
		
		mButtonSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendStringData(mEditTextInput.getText().toString());
				mEditTextInput.setText("");
			}
		});
	}

	private void overflowMenuInActionBar(){
		 try {
		        ViewConfiguration config = ViewConfiguration.get(this);
		        Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
		        if(menuKeyField != null) {
		            menuKeyField.setAccessible(true);
		            menuKeyField.setBoolean(config, false);
		        }
		    } catch (Exception ex) {
		          }
	}
	
	
	@Override
	protected void onPause() {
		mClient.cancelScan(getApplicationContext());
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		enableBluetooth();
	
	}
	
	private void initProgressDialog() {
		 mLoadingDialog = new ProgressDialog(this);
		 mLoadingDialog.setCancelable(false);
	}
	
	
	
	private void initDeviceListDialog() {
		mDeviceArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.item_device);
		ListView listView = new ListView(getApplicationContext());
		listView.setAdapter(mDeviceArrayAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String item =  (String) parent.getItemAtPosition(position); 
				for(BluetoothDevice device : mBluetoothDevices) {
					if(item.contains(device.getAddress())) {
						connect(device);
						mDeviceListDialog.cancel();
					}
				}
			}
		});
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Seleccionar Dispositivo Bluetooth");
		builder.setView(listView);
		builder.setPositiveButton("Escaner",
		 new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int id) {
			  scanDevices();
		  }
		 });
		mDeviceListDialog = builder.create();
		mDeviceListDialog.setCanceledOnTouchOutside(false);
	}
	
	private void addDeviceToArrayAdapter(BluetoothDevice device) {
		if(mBluetoothDevices.contains(device)) { 
			mBluetoothDevices.remove(device);
			mDeviceArrayAdapter.remove(device.getName() + "\n" + device.getAddress());
		}
			mBluetoothDevices.add(device);
			mDeviceArrayAdapter.add(device.getName() + "\n" + device.getAddress() );
			mDeviceArrayAdapter.notifyDataSetChanged();
		
	}
	
	private void enableBluetooth() {
		BluetoothSerialClient btSet =  mClient;
		btSet.enableBluetooth(this, new OnBluetoothEnabledListener() {
			@Override
			public void onBluetoothEnabled(boolean success) {
				if(success) {
					getPairedDevices();
				} else {
					finish();
				}
			}
		});
	}
	
	private void addText(String text) {
	    mTextView.append(text);
	    final int scrollAmount = mTextView.getLayout().getLineTop(mTextView.getLineCount()) - mTextView.getHeight();
	    if (scrollAmount > 0)
	    	mTextView.scrollTo(0, scrollAmount);
	    else
	    	mTextView.scrollTo(0, 0);
	}
		
	private void getPairedDevices() {
		Set<BluetoothDevice> devices =  mClient.getPairedDevices();
		for(BluetoothDevice device: devices) {
			addDeviceToArrayAdapter(device);
		}
	}
	
	private void scanDevices() {
		BluetoothSerialClient btSet = mClient;
		btSet.scanDevices(getApplicationContext(), new OnScanListener() {
			String message ="";
			@Override
			public void onStart() {
				Log.d("prueba", "Escaneo comienza.");
				mLoadingDialog.show();
				message = "Scanning....";
				mLoadingDialog.setMessage("Escanenado....");
				mLoadingDialog.setCancelable(true);
				mLoadingDialog.setCanceledOnTouchOutside(false);
				mLoadingDialog.setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						BluetoothSerialClient btSet = mClient;
						btSet.cancelScan(getApplicationContext());
					}
				});
			}
			
			@Override
			public void onFoundDevice(BluetoothDevice bluetoothDevice) {
				addDeviceToArrayAdapter(bluetoothDevice);
				message += "\n" + bluetoothDevice.getName() + "\n" + bluetoothDevice.getAddress();
				mLoadingDialog.setMessage(message);
			}
			
			@Override
			public void onFinish() {
				Log.d("Test", "Scan finish.");
				message = "";
				mLoadingDialog.cancel();
				mLoadingDialog.setCancelable(false);
				mLoadingDialog.setOnCancelListener(null);
				mDeviceListDialog.show();
			}
		});
	}
	
	
	private void connect(BluetoothDevice device) {
		mLoadingDialog.setMessage("Conectando....");
		mLoadingDialog.setCancelable(false);
		mLoadingDialog.show();
		BluetoothSerialClient btSet =  mClient;
		btSet.connect(getApplicationContext(), device, mBTHandler);
	}
	
	private BluetoothStreamingHandler mBTHandler = new BluetoothStreamingHandler() {
		ByteBuffer mmByteBuffer = ByteBuffer.allocate(1024);
		
		@Override
		public void onError(Exception e) {
			mLoadingDialog.cancel();
			addText("Mensaje : no se encuentra el dispositivo  !!! \n");
			mMenu.getItem(0).setTitle(R.string.action_connect);
		}
		
		@Override
		public void onDisconnected() {
			mMenu.getItem(0).setTitle(R.string.action_connect);
			mLoadingDialog.cancel();
			addText("Mensaje : Desconectado.\n");
		}
		@Override
		public void onData(byte[] buffer, int length) {
			if(length == 0) return;
			if(mmByteBuffer.position() + length >= mmByteBuffer.capacity()) {
				ByteBuffer newBuffer = ByteBuffer.allocate(mmByteBuffer.capacity() * 2); 
				newBuffer.put(mmByteBuffer.array(), 0,  mmByteBuffer.position());
				mmByteBuffer = newBuffer;
			} 
			mmByteBuffer.put(buffer, 0, length);
			if(buffer[length - 1] == '\0') {
				addText(mClient.getConnectedDevice().getName() + " <- : " +
						new String(mmByteBuffer.array(), 0, mmByteBuffer.position()) + '\n'); 
				mmByteBuffer.clear();
			}
		}
		
		@Override
		public void onConnected() {
			addText("Mensaje : Conectado. " + mClient.getConnectedDevice().getName() + "\n");
			mLoadingDialog.cancel();
			mMenu.getItem(0).setTitle(R.string.action_disconnect);
		}
	};
	
	public void sendStringData(String data) {
		data += '\0';
		byte[] buffer = data.getBytes();
		if(mBTHandler.write(buffer)) {
			addText("Android -> : " + data + '\n');
		}
	}
	
	protected void onDestroy() {
		super.onDestroy();
		mClient.claer();
	};


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		mMenu = menu;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean connect = mClient.isConnection();
		if(item.getItemId() == R.id.action_connect) {
			if (!connect) {
				mDeviceListDialog.show();
			} else {
				mBTHandler.close();
			}
			return true;
		} else {
			showCodeDlg();
			return true;
		}
	}

	private void showCodeDlg() {
		TextView codeView = new TextView(this);
		codeView.setText(Html.fromHtml(readCode()));
		codeView.setMovementMethod(new ScrollingMovementMethod());
		codeView.setBackgroundColor(Color.parseColor("#202020"));
		new AlertDialog.Builder(this, android.R.style.Theme_Holo_Light_DialogWhenLarge)
		.setView(codeView)
		.setPositiveButton("OK", new AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}).show();
	}
	
	private String readCode() {
		 try {
			InputStream is = getAssets().open("HC_06_Echo.txt");
			int length = is.available();
			byte[] buffer = new byte[length];
			is.read(buffer);
			is.close();
			String code = new String(buffer);
			buffer = null;
			return code;
		} catch (IOException e) {
			e.printStackTrace();
		}
		 return "";
	}
	

}
























