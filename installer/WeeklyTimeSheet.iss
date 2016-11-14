[Icons]
Name: {group}\Timesheet; Filename: {app}\WeeklyTimeSheet.exe; WorkingDir: {app}; IconFilename: {app}\WeeklyTimeSheet.exe
[Setup]
OutputBaseFilename=TimeSheetSetup
VersionInfoVersion=1.2
VersionInfoCompany=-
VersionInfoProductName=Weekly Timesheet
AppName=Weekly Timesheet
AppVerName=1.2
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
SetupIconFile=C:\devel\workspace\JWeeklyTimeSheet\installer\install.ico
[Files]
Source: icon_timesheet.ico; DestDir: {app}
Source: icon_timesheet.png; DestDir: {app}
Source: install.ico; DestDir: {app}
Source: install.png; DestDir: {app}
Source: uninstall.ico; DestDir: {app}
Source: uninstall.png; DestDir: {app}
Source: WeeklyTimeSheet.exe; DestDir: {app}
Source: WeeklyTimeSheet.jar; DestDir: {app}
Source: WeeklyTimeSheet_lib\h2-1.3.149.jar; DestDir: {app}\WeeklyTimeSheet_lib
Source: WeeklyTimeSheet_lib\iText-2.1.7.jar; DestDir: {app}\WeeklyTimeSheet_lib
Source: WeeklyTimeSheet_lib\JNDbm.jar; DestDir: {app}\WeeklyTimeSheet_lib
Source: WeeklyTimeSheet_lib\joda-time-1.6.2.jar; DestDir: {app}\WeeklyTimeSheet_lib
Source: WeeklyTimeSheet_lib\JRichTextEditor.jar; DestDir: {app}\WeeklyTimeSheet_lib
Source: WeeklyTimeSheet_lib\JSplitTable.jar; DestDir: {app}\WeeklyTimeSheet_lib
Source: WeeklyTimeSheet_lib\pdf-renderer-1.0.5.jar; DestDir: {app}\WeeklyTimeSheet_lib
Source: WeeklyTimeSheet_lib\swing-worker-1.2.jar; DestDir: {app}\WeeklyTimeSheet_lib
Source: WeeklyTimeSheet_lib\swingx-1.6.1.jar; DestDir: {app}\WeeklyTimeSheet_lib
Source: WeeklyTimeSheet_lib\miglayout-4.0-swing.jar; DestDir: {app}\WeeklyTimeSheet_lib
[Dirs]
Name: {app}\WeeklyTimeSheet_lib
