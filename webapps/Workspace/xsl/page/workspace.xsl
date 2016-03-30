<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:import href="../layout.xsl"/>

    <xsl:template match="/request">
        <xsl:call-template name="layout"/>
    </xsl:template>

    <xsl:template name="_content">
        <xsl:call-template name="sign-in-form"/>
        <section class="ws">
            <xsl:choose>
                <xsl:when test="//query[@entity = 'application']">
                    <xsl:apply-templates select="//query[@entity = 'application']"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:call-template name="off-app"/>
                </xsl:otherwise>
            </xsl:choose>
        </section>
    </xsl:template>

    <xsl:template match="query[@entity='application']">
        <section class="ws-apps">
            <div class="container">
                <xsl:apply-templates select="entry" mode="app"/>
            </div>
        </section>
    </xsl:template>

    <xsl:template match="entry" mode="app">
        <div class="ws-app">
            <a class="ws-app-link" href="/{viewcontent/app/@id}/{viewcontent/url}">
                <span class="ws-app-logo">
                    <img class="ws-app-logo" src="/{viewcontent/app/@id}/img/logo.png" alt="logo"/>
                </span>
                <span class="ws-app-type">
                    <xsl:value-of select="viewcontent/app/@id"/>
                </span>
                <span class="ws-app-name">
                    <xsl:value-of select="viewcontent/app"/>
                </span>
            </a>
        </div>
    </xsl:template>

    <xsl:template name="off-app">
        <section class="ws-apps off">
            <div class="container">
                <div class="ws-app off"></div>
                <div class="ws-app off"></div>
                <div class="ws-app off"></div>
            </div>
        </section>
    </xsl:template>

    <xsl:template name="sign-in-form">
        <xsl:if test="@userid = 'anonymous'">
            <form class="sign-in" action="Login" method="post">
                <h1>
                    <xsl:value-of select="//captions/sign_in/@caption"/>
                </h1>
                <label class="login">
                    <i class="fa fa-user"></i>
                    <input type="text" name="login" value="" required="required">
                        <xsl:attribute name="placeholder" select="//captions/user/@caption"/>
                    </input>
                </label>
                <label class="pwd">
                    <i class="fa fa-lock"></i>
                    <input type="password" name="pwd" value="" required="required">
                        <xsl:attribute name="placeholder" select="//captions/password/@caption"/>
                    </input>
                </label>
                <label class="noauth">
                    <input type="checkbox" name="noauth" value="1"/>
                    <span>
                        <xsl:value-of select="//captions/another_comp/@caption"/>
                    </span>
                </label>
                <button class="btn" type="submit">
                    <xsl:value-of select="//captions/login/@caption"/>
                </button>
                <div class="clearfix"></div>
                <a href="?id=reg" class="btn-sign-up">
                    <xsl:value-of select="//captions/sign_up/@caption"/>
                </a>
            </form>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>
