package xml

import com.ctc.wstx.stax.WstxOutputFactory
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import javax.xml.stream.XMLInputFactory

fun createXmlInputFactory(): XMLInputFactory {
    val inputFactory = XmlFactory.builder().xmlInputFactory()
    inputFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false)
    return inputFactory
}

fun createXmlMapper(inputFactory: XMLInputFactory?): ObjectMapper {
    return XmlMapper.builder(XmlFactory(inputFactory, WstxOutputFactory()))
        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .addModule(JacksonXmlModule())
        .defaultUseWrapper(false)
        .build()
        .registerKotlinModule()
}