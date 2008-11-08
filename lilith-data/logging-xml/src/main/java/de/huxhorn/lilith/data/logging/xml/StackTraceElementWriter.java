package de.huxhorn.lilith.data.logging.xml;

import de.huxhorn.lilith.data.logging.ExtendedStackTraceElement;
import de.huxhorn.sulky.stax.GenericStreamWriter;
import de.huxhorn.sulky.stax.StaxUtilities;

import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.XMLStreamException;

public class StackTraceElementWriter
	implements GenericStreamWriter<ExtendedStackTraceElement>, LoggingEventSchemaConstants
{
	private String prefix;
	private String preferredPrefix;
	private boolean writingSchemaLocation;

	public boolean isWritingSchemaLocation()
	{
		return writingSchemaLocation;
	}

	public void setWritingSchemaLocation(boolean writingSchemaLocation)
	{
		this.writingSchemaLocation = writingSchemaLocation;
	}

	public String getPreferredPrefix()
	{
		return preferredPrefix;
	}

	public void setPreferredPrefix(String prefix)
	{
		this.preferredPrefix = prefix;
	}

	public void write(XMLStreamWriter writer, ExtendedStackTraceElement elem, boolean isRoot) throws XMLStreamException
	{
		if(isRoot)
		{
			writer.writeStartDocument("utf-8","1.0");
		}
		StaxUtilities.NamespaceInfo ni = StaxUtilities.setNamespace(writer, preferredPrefix, NAMESPACE_URI, DEFAULT_NAMESPACE_PREFIX);

		prefix=ni.getPrefix();

		StaxUtilities.writeStartElement(writer, prefix, NAMESPACE_URI, STACK_TRACE_ELEMENT_NODE);
		if(ni.isCreated())
		{
			StaxUtilities.writeNamespace(writer, prefix, NAMESPACE_URI);
		}

		if(isRoot && writingSchemaLocation)
		{
			ni = StaxUtilities.setNamespace(writer, StaxUtilities.XML_SCHEMA_INSTANCE_PREFIX, StaxUtilities.XML_SCHEMA_INSTANCE_NAMESPACE_URI, StaxUtilities.XML_SCHEMA_INSTANCE_PREFIX);
			if(ni.isCreated())
			{
				StaxUtilities.writeNamespace(writer, ni.getPrefix(), StaxUtilities.XML_SCHEMA_INSTANCE_NAMESPACE_URI);
			}
			StaxUtilities.writeAttribute(writer,
					true,
					ni.getPrefix(),
					StaxUtilities.XML_SCHEMA_INSTANCE_NAMESPACE_URI,
					StaxUtilities.XML_SCHEMA_INSTANCE_SCHEMA_LOCATION_ATTRIBUTE,
					NAMESPACE_URI+" "+NAMESPACE_LOCATION);
		}

		//StaxUtilities.writeStartElement(writer, prefix, NAMESPACE_URI, STACK_TRACE_ELEMENT_NODE);
		StaxUtilities.writeAttribute(writer, false, prefix, NAMESPACE_URI, ST_CLASS_NAME_ATTRIBUTE, elem.getClassName());
		StaxUtilities.writeAttribute(writer, false, prefix, NAMESPACE_URI, ST_METHOD_NAME_ATTRIBUTE, elem.getMethodName());
		StaxUtilities.writeAttributeIfNotNull(writer, false, prefix, NAMESPACE_URI, ST_FILE_NAME_ATTRIBUTE, elem.getFileName());
		int lineNumber=elem.getLineNumber();
		if(lineNumber == ExtendedStackTraceElement.NATIVE_METHOD)
		{
			StaxUtilities.writeEmptyElement(writer, prefix, NAMESPACE_URI, ST_NATIVE_NODE);
		}
		else if(lineNumber>=0)
		{
			StaxUtilities.writeSimpleTextNode(writer, prefix, NAMESPACE_URI, ST_LINE_NUMBER_NODE, ""+lineNumber);
		}
		if(elem.getCodeLocation()!=null)
		{
			StaxUtilities.writeSimpleTextNode(writer, prefix, NAMESPACE_URI, ST_CODE_LOCATION_NODE, elem.getCodeLocation());
		}
		if(elem.getVersion()!=null)
		{
			StaxUtilities.writeSimpleTextNode(writer, prefix, NAMESPACE_URI, ST_VERSION_NODE, elem.getVersion());
		}
		if(elem.isExact())
		{
			StaxUtilities.writeEmptyElement(writer, prefix, NAMESPACE_URI, ST_EXACT_NODE);
		}
		writer.writeEndElement();
		if(isRoot)
		{
			writer.writeEndDocument();
		}
	}
}