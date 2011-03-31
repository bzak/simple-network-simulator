<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl"
    xmlns:g="http://graphml.graphdrawing.org/xmlns"
>
    <xsl:output method="text" indent="yes" encoding="utf-8"/>
  <xsl:template match="/">
      <xsl:apply-templates select="g:graphml/g:graph"/>
  </xsl:template>
  <xsl:template match="g:graph">
    <xsl:text>value;all_avg;in_avg;out_avg;mutual_avg;all_foaf_avg;all_foaf2_avg</xsl:text>    
	<xsl:text>&#xa;</xsl:text>
    <xsl:apply-templates select="g:node"/>
  </xsl:template>

  <xsl:template match="g:node">
    <xsl:value-of select="g:data[@key='value']"/>
    <xsl:text>;</xsl:text>
    <xsl:value-of select="g:data[@key='all_avg']"/>
    <xsl:text>;</xsl:text>
    <xsl:value-of select="g:data[@key='in_avg']"/>
    <xsl:text>;</xsl:text>
    <xsl:value-of select="g:data[@key='out_avg']"/>
    <xsl:text>;</xsl:text>
    <xsl:value-of select="g:data[@key='mutual_avg']"/>
    <xsl:text>;</xsl:text>
    <xsl:value-of select="g:data[@key='all_foaf_avg']"/>
    <xsl:text>;</xsl:text>
    <xsl:value-of select="g:data[@key='all_foaf2_avg']"/>
    <xsl:text>;</xsl:text>
	<xsl:text>&#xa;</xsl:text>
  </xsl:template>
  
  
</xsl:stylesheet>
