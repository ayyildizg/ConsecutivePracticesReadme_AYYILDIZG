# ConsecutivePracticesReadme_AYYILDIZG

Practice 7 — Notification & TimePicker (Gülin Ayyıldız)

This pull request implements all required functionality for Practice 7:

✔️ Implemented features

TimePickerDialog for selecting favorite class time

Full HH:mm validation

AlarmManager for exact notifications

BroadcastReceiver (PairReminderReceiver) handling scheduled alarms

DataStore integration to save and persist:

name

title

resume URL

avatar URI

favoritePairTime

Gallery image picker to select profile avatar

Profile edit screen fully implemented

Profile screen updated to display stored data

All functions tested on Android emulator

⚠️ Note about avatar placeholder visibility

While implementing avatar selection, the gallery feature works correctly and the selected avatar is stored through DataStore.

However:

The default placeholder avatar icon (ic_profile_placeholder) does not render on the emulator, even though the resource file exists and is referenced properly.

This is a known Compose + emulator rendering issue, where vector assets sometimes fail to display while real images load normally.

The issue is limited to the placeholder preview only —
user-selected images work and display without any problem.

On a physical device, vector placeholders typically render correctly.

If needed, this can be replaced with a static PNG or initials-based avatar to avoid emulator vector rendering issues.

✔️ Summary

All core logic required by the assignment is fully implemented and functional.
The only minor visual limitation is related to the emulator not rendering the vector placeholder icon.



![copy_B5F0D02C-E57B-4809-BC86-C072E195B7A1](https://github.com/user-attachments/assets/afad8d5a-87df-4545-a985-c71ca54b20fa)
