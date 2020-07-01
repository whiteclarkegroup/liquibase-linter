package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.google.auto.service.AutoService;
import com.whiteclarkegroup.liquibaselinter.config.rules.AbstractLintRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeLogRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.UnexpectedLiquibaseException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileReader;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AutoService({ChangeLogRule.class})
public class AllowableXsdVersionsRule extends AbstractLintRule implements ChangeLogRule {
    private static final Pattern DBCHANGELOG_DEFAULT_MATCH = Pattern.compile(".*dbchangelog-(.*).xsd");
    private static final String NAME = "allowable-xsd-versions";
    private static final String MESSAGE = "Changelog should use an XSD version in %s";
    private static final XMLInputFactory factory = XMLInputFactory.newInstance();

    private Pattern dbChangelogPattern = DBCHANGELOG_DEFAULT_MATCH;


    public AllowableXsdVersionsRule() throws ParserConfigurationException {
        super(NAME, MESSAGE);
    }

    @Override
    public void configure(RuleConfig ruleConfig) {
        super.configure(ruleConfig);
        if (ruleConfig.hasPattern()) {
            this.dbChangelogPattern = Pattern.compile(ruleConfig.getPatternString());
        }
    }

    @Override
    public boolean invalid(DatabaseChangeLog changeLog) {
        String xsdVersion = getXsdVersion(changeLog);
        return !ruleConfig.getValues().contains(xsdVersion);
    }

    private String getXsdVersion(DatabaseChangeLog changeLog) {
        try (FileReader reader = new FileReader(changeLog.getPhysicalFilePath());){
            XMLEventReader eventReader = factory.createXMLEventReader(reader);
            while (eventReader.hasNext())
            {
                XMLEvent xmlEvent = eventReader.nextEvent();
                if (xmlEvent.isStartElement()) {
                    StartElement startElement = xmlEvent.asStartElement();
                    if (startElement.getName().getLocalPart().equalsIgnoreCase("databaseChangeLog")) {
                        Iterator attributes = startElement.getAttributes();
                        while(attributes.hasNext()) {
                            Attribute attr = (Attribute)attributes.next();
                            String value = attr.getValue();
                            Matcher m = dbChangelogPattern.matcher(value);
                            if (m.matches()) {
                                return m.group(1);
                            } else {
                                throw new UnexpectedLiquibaseException("Unable to parse XSD version from "+value);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new UnexpectedLiquibaseException("Failed to process "+changeLog,e);
        }
        return null;
    }

    @Override
    public String getMessage(DatabaseChangeLog change) {
        return formatMessage(ruleConfig.getValues());
    }
}
