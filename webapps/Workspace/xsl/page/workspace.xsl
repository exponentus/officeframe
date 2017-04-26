<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html" encoding="utf-8" indent="no"/>

    <xsl:template match="/request">
        <xsl:call-template name="layout"/>
    </xsl:template>

    <xsl:template name="layout">
        <xsl:text disable-output-escaping="yes">&lt;</xsl:text>!DOCTYPE html<xsl:text
            disable-output-escaping="yes">&gt;</xsl:text>
        <html manifest="manifest.appcache?v={//build}">
            <head>
                <base href=""/>
                <meta charset="utf-8"/>
                <title>
                    <xsl:value-of select="//captions/brand/@caption"/>
                </title>
                <link rel="shortcut icon" href="img/favicon.png"/>
                <meta name="format-detection" content="telephone=no"/>
                <meta name="viewport"
                      content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
                <link rel="stylesheet" href="/SharedResources/vendor/bootstrap/css/bootstrap.min.css"/>
                <link rel="stylesheet" href="/SharedResources/vendor/font-awesome/css/font-awesome.min.css"/>
                <link rel="stylesheet" href="/SharedResources/nb/css/nb.min.css"/>
                <style>
                    <![CDATA[
                    /* fix: fieldset content overflow */
                    fieldset {
                        display: block;
                        min-width: inherit; /* chrome */
                    }
                    @-moz-document url-prefix() {
                        fieldset {
                            display: table-column !important;
                        }
                    }
                    .app-loading > .brand-logo {
                        max-height: 60px;
                    }
                    ]]>
                </style>
            </head>
            <body>
                <app class="body">
                    <div class="app-loading">
                        <img class="brand-logo" alt="logo" src="{//logo}"/>
                        <div class="three-bounce">
                            <div class="three-bounce-it one"></div>
                            <div class="three-bounce-it two"></div>
                            <div class="three-bounce-it three"></div>
                        </div>
                    </div>
                </app>
                <script src="/SharedResources/ng-app/vendor.js.gz"></script>
                <script src="/SharedResources/ng-app/app.js.gz"></script>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>
