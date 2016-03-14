[Icons]
Name: {group}\Timesheet; Filename: {app}\WeeklyTimeSheet.exe; WorkingDir: {app}; IconFilename: {app}\WeeklyTimeSheet.exe
[Setup]
OutputBaseFilename=TimeSheetSetup
VersionInfoVersion=1.2
VersionInfoCompany=-
VersionInfoProductName=Weekly Timesheet
AppName=Weekly Timesheet
AppVerName=1.1
DefaultDirName={pf}\WeeklyTimesheet
AppendDefaultGroupName=false
VersionInfoDescription=Weekly Timesheet
VersionInfoTextVersion=Weekly Timesheet
VersionInfoProductVersion=1.2
AppVersion=1.2
UninstallDisplayIcon={app}\uninstall.ico
UninstallDisplayName=Weekly Timesheet
UsePreviousGroup=false
DefaultGroupName=Timesheet
WizardImageBackColor=clGreen
SetupIconFile=C:\Devel\JWeeklyTimeSheet\installer\install.ico
[Files]
Source: icon_timesheet.ico; DestDir: {app}
Source: icon_timesheet.png; DestDir: {app}
Source: install.ico; DestDir: {app}
Source: install.png; DestDir: {app}
Source: uninstall.ico; DestDir: {app}
Source: uninstall.png; DestDir: {app}
Source: WeeklyTimeSheet.exe; DestDir: {app}
Source: WeeklyTimeSheet.jar; DestDir: {app}
Source: c:\Devel\JWeeklyTimeSheet\libraries\h2-1.3.149.jar; DestDir: {app}\lib
Source: c:\Devel\JWeeklyTimeSheet\libraries\iText-2.1.7.jar; DestDir: {app}\lib
Source: c:\Devel\JWeeklyTimeSheet\libraries\JNDbm.jar; DestDir: {app}\lib
Source: c:\Devel\JWeeklyTimeSheet\libraries\joda-time-1.6.2.jar; DestDir: {app}\lib
Source: c:\Devel\JWeeklyTimeSheet\libraries\JRichTextEditor.jar; DestDir: {app}\lib
Source: c:\Devel\JWeeklyTimeSheet\libraries\JSplitTable.jar; DestDir: {app}\lib
Source: c:\Devel\JWeeklyTimeSheet\libraries\pdf-renderer-1.0.5.jar; DestDir: {app}\lib
Source: c:\Devel\JWeeklyTimeSheet\libraries\swing-worker-1.2.jar; DestDir: {app}\lib
Source: c:\Devel\JWeeklyTimeSheet\libraries\swingx-1.6.1.jar; DestDir: {app}\lib
Source: c:\Devel\JWeeklyTimeSheet\libraries\miglayout-4.0-swing.jar; DestDir: {app}\lib
[Dirs]
Name: {app}\lib
