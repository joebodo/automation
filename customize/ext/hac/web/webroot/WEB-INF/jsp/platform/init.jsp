<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="hac" uri="/WEB-INF/custom.tld" %>
<html>
<head>
<title><c:out value="${title}" /></title>
<link rel="stylesheet" href="<c:url value="/static/css/platform/init.css"/>" type="text/css" media="screen, projection" />

<script type="text/javascript" src="<c:url value="/static/js/history.js"/>"></script>
<script type="text/javascript" src="<c:url value="/static/js/platform/init.js"/>"></script>
</head>
<body>
	<div class="prepend-top span-17 colborder" id="content">
		<button id="toggleSidebarButton">&gt;</button>
		<h2 class="marginLeft"><span id="typeHeading" data-initType="${type}">Initialization</span></h2>
		<div class="marginLeft marginBottom" id="inner" data-updateLogUrl="<c:url value="/initlog/log"/>">

			<button id="sqlScripts" data-url="<c:url value="/platform/dryrun"/>">SQL Scripts</button>
			<button id="lockButton" data-url="<c:url value="/platform/config/lock"/>">Lock</button><span id="lockInfo" data-locked="${locked}" data-unlockable="${unlockable}" data-initialized="${initialized}"></span>

			<div id="lockInfoWrapper">
				<hac:note>
					System is currently locked for initialization and update. <span id="lockAdditionalInfo"></span>
				</hac:note>
			</div>
			<div id="initUpdateForm" data-url="<c:url value="/platform/init/execute"/>">
				<button class="buttonExecute">Init Platform</button>
				<button id="dumpConfiguration" data-url="<c:url value="/platform/dumpConfiguration"/>">Dump configuration</button>
				<dl>
					<dt><spring:message code="platform.init.dbinfo.database.pool" /></dt>
					<dd><c:out value="${databaseInfoData.pool}" /></dd>

					<dt><spring:message code="platform.init.dbinfo.database.url" /></dt>
					<dd><c:out value="${databaseInfoData.url}" /></dd>

					<dt><spring:message code="platform.init.dbinfo.database.table.prefix" /></dt>
					<dd><c:out value="${databaseInfoData.tablePrefix}" /></dd>

					<dt><spring:message code="platform.init.dbinfo.database.table.name" /></dt>
					<dd><c:out value="${databaseInfoData.dbName}" /></dd>

					<dt><spring:message code="platform.init.dbinfo.database.table.user.account" /></dt>
					<dd><c:out value="${databaseInfoData.dbUser}" /></dd>
				</dl>


				<h3>General settings</h3>
				<dl>
					<dt>TenantID</dt>
					<dd id="tenantID"><c:out value="${tenantID}" /></dd>

					<dt>Master</dt>
					<dd><c:out value="${masterTenant}" /></dd>
				</dl>
				<div id="initMethodWrapper">
					<!-- specific for update / can be unchecked -->
					<input type="checkbox" id="initMethod" checked/>
					<label for="initMethod">Update running system</label><br/>
				</div>

				<!--  always hidden - for init ON for update OFF -->
				<div id="initOptions" data-initUiDataUrl="<c:url value="/platform/init/data/"/>" data-systemPatchesDataUrl="<c:url value="/platform/init/pendingPatches" />">
					<!-- c:if test="${masterTenant == true}"-->
						<input type="checkbox" id="dropTables" checked/>
						<label for="dropTables">Drop <strong>all</strong> database tables</label><br/>
					<!-- /c:if-->
				</div>

				<div id="requiredForInit">
                    <c:if test="${hmcenabled == true}">
                        <input type="checkbox" id="clearHMC" checked/>
                        <label for="clearHMC">Clear the hMC configuration from the database</label><br/>
                    </c:if>

					<input type="checkbox" id="createEssentialData" />
					<label for="createEssentialData">Create essential data</label><br/>

					<input type="checkbox" id="localizeTypes" checked/>
					<label for="localizeTypes">Localize types</label>
				</div>

				<h3>Project data settings</h3>

				<input type="checkbox" id="allProjectDataCheckbox" checked/>
				<label for="allProjectDataCheckbox">Init/Update All</label><br/>
				<hr style="margin-top:5px;"/>
				<input type="checkbox" id="toggleAll" checked/>
				<label for="toggleAll">Toggle all</label><br/>

				<div id="projectData">
                </div>

                <hr style="margin-top: 15px;margin-bottom: 5px;"/>
                <h3>Patches</h3>

                <div id="patches">
                </div>

				<button class="buttonExecute">Init Platform</button>
			</div>
		</div>
	</div>
	<div class="span-6 last" id="sidebar">
		<div class="prepend-top" id="recent-reviews">
			<c:choose>
				<c:when test="${type eq 'INIT'}">
					<h3 class="caps">
						Page description
					</h3>
					<div class="box">
						<div class="quiet">
							<!-- Init page sidebar -->
							This page enables you to initialize the hybris Multichannel Suite.
							<hr />
							<hac:warning>
							Resetting the database removes all available data from the database.
							</hac:warning>
							<hr />
							<hac:note>
							If you want to preview initialization scripts please use <a href="/platform/dryrun?init=true">SQL Scripts</a> page.
							</hac:note>
							<hr />
							<hac:note>
							<strong>Lock button</strong><br>
							Clicking <strong>Lock</strong> button disables functionality of <strong>Initialization</strong> and <strong>Update</strong> pages.
							</hac:note>
							<hr />
							<hac:note>
							Before initialization, make sure that there are no users logged in that could buy or edit items.
							</hac:note>
						</div>
					</div>
					<h3 class="caps">
						See also
					</h3>
					<div class="box">
						<div class="quiet">
						 <ul>
							 <li> <a href="${wikiInit}" target="_blank" class="quiet">Initialization Documentation</a> </li>
						</ul>
						</div>
					</div>
				</c:when>
				<c:otherwise>
					<h3 class="caps">
						Page description
					</h3>
					<div class="box">
						<div class="quiet">
							<!-- Update page sidebar -->
							This page enables you to update the hybris Multichannel Suite, but without removing available data.
							<hr />
							<hac:note>
							If you want to preview update scripts please use <a href="/platform/dryrun">SQL Scripts</a> page.
							</hac:note>
							<hr />
							<hac:note>
							<strong>Lock button</strong><br>
							Clicking <strong>Lock</strong> button disables functionality of <strong>Initialization</strong> and <strong>Update</strong> pages.
							</hac:note>
							<hr />
							<hac:note>
							Before updating and creating essential data, make sure that there are no users logged in that could buy or edit items. You also need to disable user registration.
							</hac:note>
							<hr />
							If you only want to clear hMC configuration from database and localize types, then above precautions are not needed.
						</div>
					</div>
					<h3 class="caps">
						See also
					</h3>
					<div class="box">
						<div class="quiet">
							<ul>
						 		<li> <a href="${wikiUpdate}" target="_blank" class="quiet">Update Documentation</a> </li>
							</ul>
						</div>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	<div id="updInitConfigContainer"></div>
</body>
</html>

