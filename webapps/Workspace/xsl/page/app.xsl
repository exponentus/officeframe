<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html" encoding="utf-8" indent="no"/>

    <xsl:template match="/request">
        <xsl:call-template name="layout"/>
    </xsl:template>

    <xsl:template name="layout">
        <xsl:text disable-output-escaping="yes">&lt;</xsl:text>!DOCTYPE html<xsl:text
            disable-output-escaping="yes">&gt;</xsl:text>
        <html lang="{//locale}">
            <head>
                <base href=""/>
                <meta charset="utf-8"/>
                <title>
                    <xsl:value-of select="//captions/brand/@caption"/>
                </title>
                <link rel="shortcut icon" href="img/favicon.png"/>
                <meta name="format-detection" content="telephone=no"/>
                <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
                <meta name="viewport"
                      content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
                <meta name="theme-color" content="#394865"/>
                <link rel="manifest" href="manifest.json"/>
                <link rel="stylesheet" href="/SharedResources/vendor/bootstrap-4/css/bootstrap.min.css"/>
                <link rel="stylesheet" href="/SharedResources/vendor/font-awesome/css/font-awesome.min.css"/>
                <link rel="stylesheet" href="/SharedResources/nb/css/nb.min.css"/>
            </head>
            <body>
                <noscript>
                    <h3 class="noscript">
                        <xsl:value-of select="//captions/noscript/@caption"/>
                        Sorry, but app is not available without javascript
                    </h3>
                </noscript>
                <app-root>
                    <div class="app-loading">
                        <img class="brand-logo" alt="logo" src="{//logo}"/>
                        <div class="three-bounce">
                            <div class="three-bounce-it one"></div>
                            <div class="three-bounce-it two"></div>
                            <div class="three-bounce-it three"></div>
                        </div>
                    </div>
                </app-root>
                <script src="assets/dist/vendor.js.gz?cache"></script>
                <script src="assets/dist/app.js.gz?cache"></script>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>
