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
    <xsl:text>digraph </xsl:text>    
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
    <xsl:text> [label="</xsl:text>
    <xsl:value-of select="g:data[@key='value']"/>
    <xsl:text>"</xsl:text>
	<!-- <xsl:apply-templates select="g:data" /> -->
	<xsl:text> color=</xsl:text>
	<xsl:value-of select="g:data[@key='color']"/>
	<xsl:text> </xsl:text>
	<!-- <xsl:apply-templates select="." mode="nodeType"/> -->
    <xsl:text>]&#xa;</xsl:text>
  </xsl:template>

  <xsl:template match="g:edge">
    <xsl:text>  </xsl:text>
    <xsl:value-of select="@source"/>
    <xsl:text> -> </xsl:text>
    <xsl:value-of select="@target"/>
    <xsl:text> [label="</xsl:text>
    <xsl:value-of select="@label"/>
    <xsl:apply-templates select="g:data[@key!='nazwa' and @key!='rel']"/>
    <xsl:text>"</xsl:text>
    <xsl:apply-templates select="." mode="relType"/>
    <xsl:text>]&#xa;</xsl:text>
  </xsl:template>

  <xsl:template match="g:node" mode="nodeType">    
    <xsl:choose>
      <xsl:when test="g:data[@key='value'] = '1'">
        <xsl:text> color=red </xsl:text>
      </xsl:when>
	  <xsl:otherwise>
        <xsl:text> color=grey </xsl:text>
	  </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="g:edge" mode="relType">
    <xsl:choose>
      <xsl:when test="@label = 'friend'">
        <xsl:text> color=lightgrey </xsl:text>
      </xsl:when>
      <xsl:when test="@label = 'confounding'">
        <xsl:text> color=red </xsl:text>
      </xsl:when>
      <xsl:when test="@label = 'homophily'">
        <xsl:text> color=blue </xsl:text>
      </xsl:when>
      <xsl:when test="@label = 'contagion'">
        <xsl:text> color=darkgreen </xsl:text>
      </xsl:when>
	  <xsl:when test="@label = 'close'">
        <xsl:text> color=green </xsl:text>
      </xsl:when>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="g:data">
    <xsl:text> </xsl:text>
    <xsl:value-of select="@key"/>
    <xsl:text>="</xsl:text>
    <xsl:value-of select="."/>
    <xsl:text>"</xsl:text>
  </xsl:template>
  
  
</xsl:stylesheet>
