package parser

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

class Mapper {

    companion object {
        val xmlMapper = XmlMapper.builder()
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
            .addModule(JacksonXmlModule())
            .defaultUseWrapper(false)
            .build()
            .registerKotlinModule()
    }

/*    fun test() {
        val stringToParse = "<Test><Simple><x>1</x><y>2</y></Simple></Test>"
        val finalObject = xmlMapper.readValue(stringToParse, Test::class.java)
        println(finalObject.simple.x)    }*/
}