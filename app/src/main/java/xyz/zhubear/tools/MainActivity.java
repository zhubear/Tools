package xyz.zhubear.tools;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private RadioButton radioButtonTool1_1;
    private RadioButton radioButtonTool1_2;
    private String tool_1_option_str;
    public static String URL_PATH;
    public static String WORD_PATH;
    public static final String EXTRA_OPTION = "xyz.zhubear.tools.option";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //请求读写权限
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                //请求状态码
                int REQUEST_PERMISSION_CODE = 1;
                ActivityCompat.requestPermissions(this,
                        PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }

        //获取实例
        RadioGroup radioGroupTool1 = findViewById(R.id.tool_1_option);
        radioButtonTool1_1=findViewById(R.id.tool_1_1);
        radioButtonTool1_2=findViewById(R.id.tool_1_2);
        //设置监听
        radioGroupTool1.setOnCheckedChangeListener(new RadioGroupTool1Listener());
        //数据文件
        URL_PATH=Environment.getExternalStorageDirectory().getPath()+"/urls.txt";
        WORD_PATH=Environment.getExternalStorageDirectory().getPath()+"/words.txt";
        tool_1_option_str=URL_PATH;
        File dataFile = new File(URL_PATH);
        if (dataFile.exists()) {
            Toast.makeText(MainActivity.this,
                    "链接数据文件已存在",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this,
                    "链接数据文件不存在",Toast.LENGTH_SHORT).show();
            try {
                dataFile.createNewFile();
                FileWriter writer = new FileWriter(dataFile);
                BufferedWriter out = new BufferedWriter(writer);
                out.write("https://g.igoodtv.com/\n");
                out.write("https://g.xnian.top/\n");
                out.flush(); // 把缓存区内容压入文件
                out.close();
                writer.close();
            } catch (IOException e) {
                Toast.makeText(MainActivity.this,
                        "无法创建数据文件",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        dataFile = new File(WORD_PATH);
        if (dataFile.exists()) {
            Toast.makeText(MainActivity.this,
                    "关键词数据文件已存在",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this,
                    "关键词数据文件不存在",Toast.LENGTH_SHORT).show();
            try {
                dataFile.createNewFile();
                FileWriter writer = new FileWriter(dataFile);
                BufferedWriter out = new BufferedWriter(writer);
                out.write("香港\n");
                out.write("中国\n");
                out.flush(); // 把缓存区内容压入文件
                out.close();
                writer.close();
            } catch (IOException e) {
                Toast.makeText(MainActivity.this,
                        "无法创建数据文件",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    class RadioGroupTool1Listener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId==radioButtonTool1_1.getId()){
                //显示简短的提示信息
                Toast.makeText(MainActivity.this,
                        "谷歌镜像使用"+radioButtonTool1_1.getText(),Toast.LENGTH_SHORT).show();
                tool_1_option_str=URL_PATH;
            }else if (checkedId==radioButtonTool1_2.getId()){
                Toast.makeText(MainActivity.this,
                        "谷歌镜像使用"+radioButtonTool1_2.getText(),Toast.LENGTH_SHORT).show();
                tool_1_option_str=radioButtonTool1_2.getText().toString();
            }
        }
    }

    public void toGoogleSearch(View view) {
        Intent intent = new Intent(this, GoogleActivity.class);
        intent.putExtra(EXTRA_OPTION, tool_1_option_str);
        startActivity(intent);
    }



}
