package com.graduate.zl.sd2Lts.parse;

import com.graduate.zl.sd2Lts.common.Constants;
import com.graduate.zl.sd2Lts.model.Lifeline;
import com.graduate.zl.sd2Lts.model.OccurrenceSpecificationFragment;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 2018/7/27.
 */
public class ParseXmi {

    private String fileName;

    private Document document;

    private List<Lifeline> lifelines;

    private List<OccurrenceSpecificationFragment> osFraments;

    public ParseXmi(String fileName) {
        this.fileName = fileName;
        this.document = load();
        this.lifelines = new ArrayList<Lifeline>();
    }

    public Document load() {
        Document document = null;
        try {
            SAXReader saxReader = new SAXReader();
            document = saxReader.read(new File(this.fileName));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return document;
    }

    public void parseXmi() {
        Element root = this.document.getRootElement();
        List<Element> firstLevel = root.elements();
        Element UML_MODEL = firstLevel.get(1);
        Element UML_PACKAGE = (Element) UML_MODEL.elements().get(0);
        Element UML_COLLABORATION = (Element) UML_PACKAGE.elements().get(0);
        Element ownedBehavior = (Element) UML_COLLABORATION.elements().get(0);
        List<Element> allElements = ownedBehavior.elements();
        for(Element element : allElements) {
            String elementName = element.getName();
            if (elementName.equals(Constants.LIFELINE)) {
                Lifeline lifeline = new Lifeline(element.attribute("id").getValue(), element.attribute("name").getValue());
                this.lifelines.add(lifeline);
            } else if(elementName.equals(Constants.FRAGMENT)) {
                String type = element.attribute("type").getValue();
                if(type.equals(Constants.OCCURRENCE_SPECIFICATION)) {
                    OccurrenceSpecificationFragment osf = new OccurrenceSpecificationFragment(element.attribute("id").getValue(), element.attribute("covered").getValue());
                    this.osFraments.add(osf);
                }else if(type.equals(Constants.COMBINED_FRAGMENT)){

                }
            }
//            System.out.println(element.getName() + element.attribute("type").getValue());
        }
    }

    public static void main(String[] args) {
        ParseXmi parseXmi = new ParseXmi("C:\\Users\\Vincent\\Desktop\\test.xml");
        parseXmi.parseXmi();
    }
}