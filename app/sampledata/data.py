class Event:

    def __init__(self):
        self.start_date = []
        self.start_time = []
        self.end_date = []
        self.end_time = []
        self.location = ""
        self.freq = ""
        self.count = 0
        self.days = []
        self.summary = ""

    def print(self):
        print("start_date:", self.start_date)
        print("start_time:", self.start_time)
        print("end_date:", self.end_date)
        print("end_time:", self.end_time)
        print("location:", self.location)
        print("freq:", self.freq)
        print("count:", self.count)
        print("days:", self.days)
        print("summary:", self.summary)
        print()



events = []
event = Event()
first = True

with open('w2018sample.ics') as file:
    for line in file:
        line = line.strip()
        if "BEGIN:VEVENT" in line:
            if not first:
                events.append(event)
            first = False
            event = Event()

        elif "DTSTART" in line:
            if "DTSTART;" in line:
                line = line.split("=")[1]
            line = line.split(":")[1]
            date = line.split("T")[0]
            time = line.split("T")[1]
            event.start_date = [int(date[0:4]), int(date[4:6]), int(date[6:8])]
            event.start_time = [int(time[0:2]), int(time[2:4]), int(time[4:6])]

        elif "DTEND" in line:
            if "DTEND;" in line:
                line = line.split("=")[1]
            line = line.split(":")[1]
            date = line.split("T")[0]
            time = line.split("T")[1]
            event.end_date = [int(date[0:4]), int(date[4:6]), int(date[6:8])]
            event.end_time = [int(time[0:2]), int(time[2:4]), int(time[4:6])]

        elif "LOCATION:" in line:
            event.location = line.split(":")[1]

        elif "SUMMARY:" in line:
            event.summary = line.split(":")[1]

        elif "RRULE:" in line:
            line = line.split(":")[1].split(";")
            event.freq = line[0].split("=")[1]
            event.count = line[1].split("=")[1]
            event.days = line[2].split("=")[1].split(",")


for obj in events:
    obj.print()