package com.phaytran.xmppAndroidClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView rv;
    private Adapter adapter;
    private ArrayList<MessageData> messageData = new ArrayList<>();
    private AbstractXMPPConnection abstractXMPPConnection;
    private EditText edtMessage;
    private Button btnSendMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv=findViewById(R.id.rv);
        adapter = new Adapter(messageData);
        edtMessage = findViewById(R.id.sendMessageEt);
        btnSendMessage = findViewById(R.id.send);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration decoration = new DividerItemDecoration(this,layoutManager.getOrientation());
        rv.addItemDecoration(decoration);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        setConnection();
        btnSendMessage.setOnClickListener(this);
    }

    private void setConnection(){

        new Thread(){
            @Override
            public void run() {

        InetAddress address = null;
        try {
            address = InetAddress.getByName("192.168.100.30");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        HostnameVerifier verifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return false;
            }
        };
        DomainBareJid serviceName = null;
        try {
            serviceName = JidCreate.domainBareFrom("192.168.100.30");
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
        XMPPTCPConnectionConfiguration connectionConfiguration = XMPPTCPConnectionConfiguration.builder()
            .setUsernameAndPassword("username","password")
            .setPort(6060)
            .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
            .setXmppDomain(serviceName)
            .setHostnameVerifier(verifier)
            .setHostAddress(address)
            .setDebuggerEnabled(true)
            .build();

        abstractXMPPConnection = new XMPPTCPConnection(connectionConfiguration);

        try {
            abstractXMPPConnection.connect();
            abstractXMPPConnection.login();
            if(abstractXMPPConnection.isAuthenticated() && abstractXMPPConnection.isConnected()){
                ChatManager chatManager = ChatManager.getInstanceFor(abstractXMPPConnection);
                chatManager.addListener(new IncomingChatMessageListener() {
                    @Override
                    public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                        Log.e("Message","New message from: "+ from +": " +message.getBody());
                        MessageData data = new MessageData("received",message.getBody().toString());
                        messageData.add(data);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new Adapter(messageData);
                                rv.setAdapter(adapter);
                            }
                        });
                    }
                });

            }
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMPPException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        }
        }.start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send:
                String message = edtMessage.getText().toString();
                if(message.length()>0){
                    sendMessage(message,"username@ip");
                }
                break;
        }
    }

    private void sendMessage(String messageSend, String s) {
        EntityBareJid jid = null;
        try {
            jid = JidCreate.entityBareFrom(s);
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
        if(abstractXMPPConnection!=null) {
            ChatManager chatManager = ChatManager.getInstanceFor(abstractXMPPConnection);
            Chat chat = chatManager.chatWith(jid);
            Message  message = new Message();
            message.setBody(messageSend);
            try {
                chat.send(message);
                MessageData data = new MessageData("send",messageSend);
                messageData.add(data);
                adapter = new Adapter(messageData);
                rv.setAdapter(adapter);
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
