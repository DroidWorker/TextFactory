package com.example.textfactory.xmlHandler;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.textfactory.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class XMLparser {
    public static void parse(HashMap<Integer, String> letterData, String fontName, Context ctx)//takes HashMap<letterCode, SVGPath> & save res SVG file to temp dir
    //read samplefile line by line & if line contains needle data, replace this line and write in new file else write old line in new file
    {
        try
        {
            AssetManager assetManager = ctx.getAssets();
            InputStream in = assetManager.open("files/font_sample.xml");
            FileOutputStream fos = ctx.openFileOutput("resSVGfont.xml", Context.MODE_PRIVATE);
            byte[] buffer = new byte[1024];
            int bufferLength = 0;
            while ((bufferLength = in.read(buffer))>0)
            {
                fos.write(buffer, 0, bufferLength);
            }
            in.close();
            fos.close();

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            File f = new File("data/data/"+ctx.getPackageName()+"/files/resSVGfont.xml");
            Document doc = documentBuilder.parse(f);
            doc.getDocumentElement().normalize();
            NodeList nl = doc.getElementsByTagName("glyph");
            Element el = null;
            for (int i=0; i<nl.getLength(); i++){
                el = (Element) nl.item(i);
                if (el.hasAttribute("unicode")&&letterData.containsKey((int)((el.getAttribute("unicode")).charAt(0))))
                {
                    el.setAttribute("d", letterData.get((int)((el.getAttribute("unicode")).charAt(0))));
                }
            }
            if (fontName!=null&&fontName.length()>1){
                Toast.makeText(ctx, fontName, Toast.LENGTH_SHORT).show();
                nl = doc.getElementsByTagName("font");
                el = (Element)(nl.item(0));
                el.setAttribute("id", fontName);
                ((Element)(((Element)nl.item(0)).getElementsByTagName("font-face").item(0))).setAttribute("font-family", fontName);
            }
            doc.getDocumentElement().normalize();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("data/data/"+ctx.getPackageName()+"/files/resSVGfont.svg"));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
            f.delete();
        }
        catch (Exception ex)
        {
            Log.e("XMLparserException: ", ex.getMessage());
        }
    }
}
