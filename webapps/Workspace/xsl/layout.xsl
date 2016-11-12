<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:import href="templates/constants.xsl"/>
    <xsl:import href="templates/actions.xsl"/>

    <xsl:output method="html" encoding="utf-8" indent="no"/>

    <xsl:template name="layout">
        <xsl:param name="title" select="//org"/>
        <xsl:param name="include_head" select="''"/>

        <xsl:call-template name="HTML-DOCTYPE"/>
        <html>
            <xsl:call-template name="html-head">
                <xsl:with-param name="title" select="$title"/>
                <xsl:with-param name="include" select="$include_head"/>
            </xsl:call-template>
            <body class="workspace no_transition">
                <div class="main-load" id="main-load" style="display:none"></div>
                <div class="layout">
                    <div class="content-overlay" id="content-overlay"></div>
                    <xsl:call-template name="main-header"/>
                    <main class="container">
                        <section class="content">
                            <xsl:call-template name="_content"/>
                        </section>
                    </main>
                    <xsl:call-template name="main-footer"/>
                </div>
            </body>
        </html>
    </xsl:template>

    <xsl:template name="_content"/>

    <xsl:template name="html-head">
        <xsl:param name="title" select="''"/>
        <xsl:param name="include" select="''"/>
        <head>
            <title>
                <xsl:value-of select="$title"/>
            </title>
            <link rel="shortcut icon" href="img/favicon.png"/>
            <meta name="format-detection" content="telephone=no"/>
            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>

            <link rel="stylesheet" href="/SharedResources/vendor/bootstrap/css/bootstrap.min.css"/>
            <link rel="stylesheet" href="/SharedResources/vendor/font-awesome/css/font-awesome.min.css"/>
            <link rel="stylesheet" href="/SharedResources/vendor/jquery/jquery-ui-1.11.4.custom/jquery-ui.min.css"/>
            <link rel="stylesheet" href="/SharedResources/nb/css/nb.min.css"/>
            <link rel="stylesheet" href="css/ws.css"/>

            <script src="/SharedResources/vendor/jquery/jquery-2.1.4.min.js"></script>
            <script src="/SharedResources/vendor/jquery/jquery-ui-1.11.4.custom/jquery-ui.min.js"></script>
            <script src="/SharedResources/vendor/bootstrap/js/bootstrap.min.js"></script>
            <script src="/SharedResources/nb/js/nb.min.js"></script>
            <script src="js/app.js"></script>

            <xsl:copy-of select="$include"/>
        </head>
    </xsl:template>

    <xsl:template name="main-header">
        <header class="header navbar">
            <div class="container">
                <div class="navbar-header">
                    <span class="brand-title">
                        <xsl:value-of select="//org"/>
                    </span>
                </div>
                <xsl:if test="@userid != 'anonymous'">
                    <nav class="navbar-nav navbar-right">
                        <ul class="nav">
                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                    <i class="fa fa-user"></i>
                                    <span>
                                        <xsl:value-of select="@username"/>
                                    </span>
                                </a>
                                <ul class="dropdown-menu right">
                                    <li>
                                        <a class="user-profile_ws" href="p?id=userprofile">
                                            <i class="fa fa-user"></i>
                                            <span>
                                                <xsl:value-of select="//captions/your_profile/@caption"/>
                                            </span>
                                        </a>
                                    </li>
                                    <li class="divider"></li>
                                    <li>
                                        <a class="logout" href="Logout">
                                            <i class="fa fa-sign-out"></i>
                                            <xsl:value-of select="//captions/logout/@caption"/>
                                        </a>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                    </nav>
                </xsl:if>
            </div>
        </header>
    </xsl:template>

    <xsl:template name="main-footer">
        <div class="footer-spacer"></div>
        <footer class="footer">
            <div class="container">
                <xsl:apply-templates select="//query[@entity = 'language']"/>
                <div class="pull-left">
                    <span>v.</span>
                    <span>
                        <xsl:value-of select="//serverversion"/>
                    </span>
                    <span>build:</span>
                    <span>
                        <xsl:value-of select="//build"/>
                    </span>
                </div>
            </div>
        </footer>
    </xsl:template>

    <xsl:template match="query[@entity = 'language']">
        <div class="lang pull-right">
            <!--<a href="#" class="lang-title">
                <xsl:value-of select="//@lang"/>
            </a>-->
            <div class="lang-menu">
                <xsl:apply-templates select="entry" mode="lang"/>
            </div>
        </div>
    </xsl:template>

    <xsl:template match="entry" mode="lang">
        <a href="p?id={//request/@id}&amp;lang={viewcontent/lang/@id}">
            <xsl:if test="//request/@lang = viewcontent/lang/@id">
                <xsl:attribute name="class" select="'active'"/>
            </xsl:if>
            <xsl:value-of select="viewcontent/lang"/>
        </a>
    </xsl:template>

</xsl:stylesheet>
