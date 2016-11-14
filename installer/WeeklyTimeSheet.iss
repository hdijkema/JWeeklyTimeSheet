[Icons]
Name: {group}\Timesheet; Filename: {app}\WeeklyTimeSheet.exe; WorkingDir: {app}; IconFilename: {app}\WeeklyTimeSheet.exe
[Setup]
OutputBaseFilename=TimeSheetSetup
VersionInfoVersion=1.3
VersionInfoCompany=-
VersionInfoProductName=Weekly Timesheet
AppName=Weekly Timesheet
AppVerName=1.3
DefaultDirName={pf}\WeeklyTimesheet
AppendDefaultGroupName=false
VersionInfoDescription=Weekly Timesheet
VersionInfoTextVersion=Weekly Timesheet
VersionInfoProductVersion=1.3
AppVersion=1.3
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
Source: C:\devel\workspace\JWeeklyTimeSheet\installer\WeeklyTimeSheet_lib\*.jar; DestDir: {app}\lib
[Dirs]
Name: {app}\lib
