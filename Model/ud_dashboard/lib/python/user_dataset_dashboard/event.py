import json
import datetime
import paths


class Event:
    """
    This object holds information about irods events, which are saved as data objects in the events path under
    workspaces.
    """

    def __init__(self, event_json):
        """
        Populate a new event object with the data obtained from an event data object which is in a json format.  Note
        that the event id is a concatenation of a timestamp in sec and an 8 digit pid.
        :param event_json: event data object
        """
        event_data = json.loads(event_json)
        self.event = event_data["event"]
        self.action = event_data.get("action")
        self.recipient_id = event_data.get("recipient")
        self.dataset_id = event_data["datasetId"]
        self.event_id = event_data["eventId"]
        self.event_date = str(self.event_id)[:-8]

    def display(self, dashboard):
        """
        Provides a display of the event contained within this Event object.  The action and recipient properties
        only have meaning in the case of a share or unshare event and are otherwise not shown.
        :param dashboard:
        """
        if self.event == "share":
            recipient = dashboard.find_user_by_id(self.recipient_id)
            print("Event id {} at {} for user dataset id {}: {} - action {} - recipient {} ({}) - {}"
                .format(self.event_id, self.dataset_id,
                datetime.datetime.fromtimestamp(int(self.event_date)).strftime('%Y-%m-%d %H:%M:%S'),
                self.event, self.action, recipient.full_name, recipient.email, self.recipient_id))
        else:
            print("Event id {} at {} for user dataset id {}: {}"
                .format(self.event_id, self.dataset_id,
                datetime.datetime.fromtimestamp(int(self.event_date)).strftime('%Y-%m-%d %H:%M:%S'),
                self.event))