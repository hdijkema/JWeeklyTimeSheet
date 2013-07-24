

all: WeeklyTimeSheet.jar
	make clean
	mkdir -p /tmp/weeklytimesheet
	cp WeeklyTimeSheet.jar /tmp/weeklytimesheet
	cp WeeklyTimeSheet.sh /tmp/weeklytimesheet
	cp *.exe /tmp/weeklytimesheet
	cp *.png /tmp/weeklytimesheet
	cp *.ico /tmp/weeklytimesheet
	chmod 755 /tmp/weeklytimesheet/WeeklyTimeSheet.sh
	mkdir -p /tmp/weeklytimesheet/libraries
	cp ../libraries/* /tmp/weeklytimesheet/libraries

clean:
	rm -rf /tmp/weeklytimesheet
	
