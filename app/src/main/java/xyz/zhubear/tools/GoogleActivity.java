package xyz.zhubear.tools;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class GoogleActivity extends AppCompatActivity {
    public static List<String> urlList = new ArrayList<>();
    public static List<String> wordList = new ArrayList<>();
    public static String selectedUrl;
    public static String selectedWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google);

        Intent intent = getIntent();
        String option = intent.getStringExtra(MainActivity.EXTRA_OPTION);
        if (option.startsWith("/storage"))
            this.getUrlFormFile();
        if (option.startsWith("http"))
            this.getUrlFormUrl();
    }



    private void getUrlFormFile() {
        urlList.clear();
        urlList = this.readFile(MainActivity.URL_PATH);
        wordList.clear();
        wordList = this.readFile(MainActivity.WORD_PATH);
        Spinner urlDown = findViewById(R.id.urlList);
        //适配器
        ArrayAdapter<String> urlAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, urlList);
        //设置样式
        urlAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        urlDown.setAdapter(urlAdapter);
        //添加监听器，为下拉列表设置事件的响应
        urlDown.setOnItemSelectedListener(new MySpinnerSelectedListener());
        urlDown.setVisibility(View.VISIBLE);


        Spinner wordDown = findViewById(R.id.wordList);
        //适配器
        ArrayAdapter<String> wordAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, wordList);
        //设置样式
        wordAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        wordDown.setAdapter(wordAdapter);
        //添加监听器，为下拉列表设置事件的响应
        wordDown.setOnItemSelectedListener(new MySpinnerSelectedListener());
        wordDown.setVisibility(View.VISIBLE);

    }

    private void getUrlFormUrl() {
        TextView generateText=findViewById(R.id.generateText);
        generateText.setText("未适配该选项");
    }

    class MySpinnerSelectedListener implements Spinner.OnItemSelectedListener{
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (parent.getId()==R.id.urlList) {
                GoogleActivity.selectedUrl=GoogleActivity.urlList.get(position);
                Toast.makeText(GoogleActivity.this,
                        "下拉列表选中"+GoogleActivity.selectedUrl,Toast.LENGTH_SHORT).show();
            }
            if (parent.getId()==R.id.wordList) {
                GoogleActivity.selectedWord=GoogleActivity.wordList.get(position);
                Toast.makeText(GoogleActivity.this,
                        "下拉列表选中"+GoogleActivity.selectedWord,Toast.LENGTH_SHORT).show();
            }
        }
        public void onNothingSelected(AdapterView<?> arg0) {
            Toast.makeText(GoogleActivity.this,
                    "下拉列表未选中",Toast.LENGTH_SHORT).show();
        }
    }

    public void addUrl(View view) {
        EditText editText = findViewById(R.id.editTextURL);
        String text = editText.getText().toString();
        if (text.startsWith("http")) {
            urlList.add(text);
            this.writeFile(MainActivity.URL_PATH,urlList);
            Toast.makeText(GoogleActivity.this,
                    text+"已添加到链接文件",Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(GoogleActivity.this,
                    text+"格式不正确",Toast.LENGTH_SHORT).show();
    }
    public void delUrl(View view) {
        EditText editText = findViewById(R.id.editTextURL);
        String text = editText.getText().toString();
        if (urlList.contains(text)) {
            urlList.remove(text);
            this.writeFile(MainActivity.URL_PATH,GoogleActivity.urlList);
            Toast.makeText(GoogleActivity.this,
                    text+"已从链接文件中删除",Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(GoogleActivity.this,
                    text+"不在文件中",Toast.LENGTH_SHORT).show();
    }
    public void addWord(View view) {
        EditText editText = findViewById(R.id.editTextWord);
        String text = editText.getText().toString();
        if (!text.isEmpty()) {
            GoogleActivity.wordList.add(text);
            this.writeFile(MainActivity.WORD_PATH,wordList);
            Toast.makeText(GoogleActivity.this,
                    text+"已添加搜索词到文件",Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(GoogleActivity.this,
                    "搜索词为空",Toast.LENGTH_SHORT).show();
    }
    public void delWord(View view) {
        EditText editText = findViewById(R.id.editTextWord);
        String text = editText.getText().toString();
        if (GoogleActivity.wordList.contains(text)) {
            GoogleActivity.wordList.remove(text);
            this.writeFile(MainActivity.WORD_PATH,wordList);
            Toast.makeText(GoogleActivity.this,
                    text+"已从搜索词文件中删除",Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(GoogleActivity.this,
                    text+"不在文件中",Toast.LENGTH_SHORT).show();
    }
    public void generate(View view) {
        TextView generateText=findViewById(R.id.generateText);
        String url="";
        try {
            url = selectedUrl + "search?q=";
            url=url+java.net.URLEncoder.encode(selectedWord,"utf-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        generateText.setText(url);
    }


    /**
     * 读入TXT文件
     */
    private List<String> readFile(String filePath) {
        List<String> dataList = new ArrayList<>();
        //防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw;
        //不关闭文件会导致资源的泄露，读写文件都同理
        try {
            FileReader reader = new FileReader(filePath);
            BufferedReader br = new BufferedReader(reader);
            String line;
            //网友推荐更加简洁的写法
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty())
                    dataList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * 写入TXT文件
     */
    private void writeFile(String filePath, List<String> dataList) {
        // 相对路径，如果没有则要建立一个新的output.txt文件
        File writeFile= new File(filePath);
        try {
            // 创建新文件,有同名的文件的话直接覆盖
            writeFile.createNewFile();
            FileWriter writer = new FileWriter(writeFile);
            BufferedWriter out = new BufferedWriter(writer);
            for (String line:dataList){
                if (!line.isEmpty())
                    out.write(line+"\n");
            }
            out.flush(); // 把缓存区内容压入文件
            out.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
