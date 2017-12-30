<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/TR/WD-xsl">
<xsl:template match="/">
  <html>
  	<head>
  		<title>------------------------------------</title>
  	</head>
  <body>
    
    <xsl:for-each select="Frases/Frase">
      "<B>	<xsl:value-of select="Texto"/>	</B>"
	<br/>
    	<xsl:value-of select="Autor"/>.
	<I>	<xsl:value-of select="Libro"/>	</I>
	<br/>
	<xsl:value-of select="Otro"/>
	<br/>
	<br/>
      </xsl:for-each>	
  </body>
  </html>
</xsl:template>
</xsl:stylesheet>
 
