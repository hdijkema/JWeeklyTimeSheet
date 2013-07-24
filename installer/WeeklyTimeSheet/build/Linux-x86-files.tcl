# options: -translation lf -encoding utf-8

proc ::InstallJammer::InitFiles {} {
    File ::8C7C30C8-C1F5-7839-3509-34CC387E1740 -name weeklytimesheet -parent 1FD33859-FE76-F52E-46A1-A90F3E58DF98 -directory <%InstallDir%> -type dir -permissions 040755 -filemethod 0
    File ::E9A07E11-4C91-0FE7-5D00-8A88080EFE14 -name WeeklyTimeSheet.jar -parent 1FD33859-FE76-F52E-46A1-A90F3E58DF98 -directory <%InstallDir%> -size 184929 -mtime 1298241395 -permissions 00644 -filemethod 0
    File ::8D646A3E-89C3-B247-E71F-FB7E7FA19338 -name WeeklyTimeSheet.sh -parent 1FD33859-FE76-F52E-46A1-A90F3E58DF98 -directory <%InstallDir%> -size 262 -mtime 1298241395 -permissions 00755 -filemethod 0
    File ::6CA98CBC-1F24-0D19-9446-2A7C7ADBA8F3 -name libraries -parent 1FD33859-FE76-F52E-46A1-A90F3E58DF98 -directory <%InstallDir%>/libraries -type dir -permissions 040755 -filemethod 0
    File ::36E05277-620D-549E-8478-E68A602930A6 -name h2-1.3.149.jar -parent 1FD33859-FE76-F52E-46A1-A90F3E58DF98 -directory <%InstallDir%>/libraries -size 1238967 -mtime 1298241395 -permissions 00644 -filemethod 0
    File ::6888F8A4-EED6-BB2C-852F-38FB948116E6 -name iText-2.1.7.jar -parent 1FD33859-FE76-F52E-46A1-A90F3E58DF98 -directory <%InstallDir%>/libraries -size 1130070 -mtime 1298241395 -permissions 00644 -filemethod 0
    File ::3FE402D7-996F-9C43-ECF6-6839BD16CBB0 -name JNDbm.jar -parent 1FD33859-FE76-F52E-46A1-A90F3E58DF98 -directory <%InstallDir%>/libraries -size 80688 -mtime 1298241395 -permissions 00644 -filemethod 0
    File ::B4A72120-F9FA-C998-2DA0-7C1717D49769 -name joda-time-1.6.2.jar -parent 1FD33859-FE76-F52E-46A1-A90F3E58DF98 -directory <%InstallDir%>/libraries -size 543044 -mtime 1298241395 -permissions 00644 -filemethod 0
    File ::FE37296E-CBA2-34A3-755F-2F0E3EDA53F5 -name JRichTextEditor.jar -parent 1FD33859-FE76-F52E-46A1-A90F3E58DF98 -directory <%InstallDir%>/libraries -size 975609 -mtime 1298241395 -permissions 00644 -filemethod 0
    File ::CA9BDC6A-B601-B55D-F7F2-2678F8D6989E -name JSplitTable.jar -parent 1FD33859-FE76-F52E-46A1-A90F3E58DF98 -directory <%InstallDir%>/libraries -size 56236 -mtime 1298241395 -permissions 00644 -filemethod 0
    File ::AE4A4DE3-F9DC-701A-CE12-05B1F44C92F7 -name miglayout-3.7.3.1.jar -parent 1FD33859-FE76-F52E-46A1-A90F3E58DF98 -directory <%InstallDir%>/libraries -size 201062 -mtime 1298241396 -permissions 00644 -filemethod 0
    File ::BC850C62-6F54-6D91-A9AA-DB78730B5533 -name pdf-renderer-1.0.5.jar -parent 1FD33859-FE76-F52E-46A1-A90F3E58DF98 -directory <%InstallDir%>/libraries -size 2087109 -mtime 1298241396 -permissions 00644 -filemethod 0
    File ::67898628-1DAA-72DE-0309-9C455FC7E85A -name swing-worker-1.2.jar -parent 1FD33859-FE76-F52E-46A1-A90F3E58DF98 -directory <%InstallDir%>/libraries -size 12852 -mtime 1298241396 -permissions 00644 -filemethod 0
    File ::F6911DC3-382F-4629-B358-319D87040894 -name swingx-1.6.1.jar -parent 1FD33859-FE76-F52E-46A1-A90F3E58DF98 -directory <%InstallDir%>/libraries -size 1389926 -mtime 1298241396 -permissions 00644 -filemethod 0
    File ::A91C62DB-D691-90DA-3421-28B1CE0461A8 -name icon_timesheet.png -parent 1FD33859-FE76-F52E-46A1-A90F3E58DF98 -directory <%InstallDir%> -size 17407 -mtime 1298241395 -permissions 00644 -filemethod 0
    File ::52E9DB6A-9559-1C10-CED7-20795A493F1C -name install.ico -parent 1FD33859-FE76-F52E-46A1-A90F3E58DF98 -directory <%InstallDir%> -size 171014 -mtime 1298241395 -permissions 00644 -filemethod 0
    File ::21ADCEF8-FA4C-C4CA-6224-8AFF10F4BBD0 -name install.png -parent 1FD33859-FE76-F52E-46A1-A90F3E58DF98 -directory <%InstallDir%> -size 6914 -mtime 1298241395 -permissions 00644 -filemethod 0
    File ::DD2A3BD2-F1CB-B356-54E8-AEAE25B49A7F -name uninstall.ico -parent 1FD33859-FE76-F52E-46A1-A90F3E58DF98 -directory <%InstallDir%> -size 171014 -mtime 1298241395 -permissions 00644 -filemethod 0
    File ::790A20B1-5FF9-3407-3594-4983CEF82EAB -name uninstall.png -parent 1FD33859-FE76-F52E-46A1-A90F3E58DF98 -directory <%InstallDir%> -size 7311 -mtime 1298241395 -permissions 00644 -filemethod 0
    File ::F432639A-5416-F042-ECD0-03DA8B1FE78E -name icon_timesheet.ico -parent 1FD33859-FE76-F52E-46A1-A90F3E58DF98 -directory <%InstallDir%> -size 159190 -mtime 1298241395 -permissions 00644 -filemethod 0
    File ::537801D0-5627-5748-04BC-E6FF65D9A3A8 -name WeeklyTimeSheet.exe -parent 1FD33859-FE76-F52E-46A1-A90F3E58DF98 -directory <%InstallDir%> -size 186368 -mtime 1298241395 -permissions 00755 -filemethod 0

}
