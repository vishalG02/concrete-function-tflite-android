package com.vis.concretefunction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editText = findViewById(R.id.editText);
        Button result = findViewById(R.id.button);
        final TextView res = findViewById(R.id.textView);

        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Interpreter interpreter = new Interpreter(loadModelFile());
                    float[] input = new float[1];
                    input[0] = Float.parseFloat(editText.getText().toString());

                    float[] output = new float[1];
                    interpreter.run(input,output);
                    res.setText(output[0]+"");
                } catch (IOException e) {
                    e.printStackTrace();
                    res.setText(e.getMessage());
                }
            }
        });
    }

    private MappedByteBuffer loadModelFile() throws IOException
    {
        AssetFileDescriptor assetFileDescriptor = this.getAssets().openFd("concrete_function.tflite");
        FileInputStream fileInputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());
        FileChannel fileChannel = fileInputStream.getChannel();
        long startOffset = assetFileDescriptor.getStartOffset();
        long length = assetFileDescriptor.getLength();
        return  fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,length);
    }
}
