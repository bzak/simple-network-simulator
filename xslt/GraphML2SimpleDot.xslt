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
    <xsl:text>graph </xsl:text>    
    <xsl:value-of select="@id" />
    <xsl:text> {&#xa;</xsl:text>
    <xsl:text>  node [fontsize=10]&#xa;</xsl:text>
    <xsl:text>  edge [fontsize=10]&#xa;</xsl:text>
    <xsl:apply-templates select="g:node"/>
    <xsl:text>&#xa;</xsl:text>
    <xsl:apply-templates select="g:edge"/>
    <xsl:text>}</xsl:text>
  </xsl:template>

  <xsl:template match="g:node">
    <xsl:text>  </xsl:text>
    <xsl:value-of select="@id"/>
    <xsl:text>&#xa;</xsl:text>
  </xsl:template>

  <xsl:template match="g:edge">
    <xsl:text>  </xsl:text>
    <xsl:value-of select="@source"/>
    <xsl:text> -- </xsl:text>
    <xsl:value-of select="@target"/>
    <xsl:text>&#xa;</xsl:text>
  </xsl:template>

  
  <xsl:template match="g:data">
    <xsl:text>\n</xsl:text>
    <xsl:value-of select="@key"/>
    <xsl:text>=</xsl:text>
    <xsl:value-of select="."/>
  </xsl:template>
  
  
</xsl:stylesheet>
