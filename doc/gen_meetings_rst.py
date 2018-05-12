"""
Script to generate meeting entries from our meeting markdown documents to
include into the documentation.
"""

import os.path
import os
import re

gen_dir = "generated/meetings"
minutes_rst_dir = os.path.join(gen_dir, "minutes")
agendas_rst_dir = os.path.join(gen_dir, "agendas")
gen_minutes_file = os.path.join(gen_dir, "minutes.rst")
gen_agendas_file = os.path.join(gen_dir, "agendas.rst")

if not os.path.exists(gen_dir):
    os.makedirs(gen_dir)


# match of the format MeetingMinutes_24.1_Group_2030_11_8_2016.rst
meeting_re = re.compile(r'''^[A-Za-z]+[_](?P<number>[0-9.]+)[_](?P<type>[a-zA-Z]+)[_](?P<time>\d+)[_](?P<day>\d+)[_](?P<month>\d+)[_](?P<year>\d+).*$''')

class MeetingFile:
    def __init__(self, filename, filepath, number, time, day, month, year, meeting_type):
        self.filename = filename
        self.filepath = filepath
        self.number = number
        self.time = time
        self.day = day
        self.month = month
        self.year = year
        self.meeting_type = meeting_type

def fill_dictionary(dictionary, directory):
    filenames = os.listdir(directory)
    for filename in filenames:
        match = meeting_re.match(filename)
        if match:
            mf = MeetingFile(filename,
                             os.path.join(directory, filename),
                             float(match.group("number")),
                             match.group("time"),
                             match.group("day"),
                             match.group("month"),
                             match.group("year"),
                             match.group("type"))
            dictionary[mf.number] = mf
        else:
            print("WARNING non matching file", filename)

def write_download_entries(dictionary, f):
    for i in sorted(dictionary):
        mf = dictionary[i]
        f.write("+ :download:`{0} <{1}>`\n\n".format(mf.filename, "/" + mf.filepath))

def write_toc_entries(f, dictionary, prefix):
    for i in sorted(dictionary):
        mf = dictionary[i]
        format_dic = {
            "prefix": prefix,
            "filepath": "/" + mf.filepath,
            "number": str(mf.number).rstrip('0').rstrip('.'),
            "day": mf.day,
            "month": mf.month,
            "year": mf.year,
            "type": mf.meeting_type
        }
        f.write("\t{prefix} {number} ({type}) - {day}/{month}/{year} <{filepath}>\n".format(**format_dic))

f = open(gen_minutes_file, "w")
f.write("""\
Meeting Minutes
===============

.. toctree::
\t:caption: Minutes
\t:maxdepth: 1

""")


minutes_dic = {}
fill_dictionary(minutes_dic, minutes_rst_dir)
write_toc_entries(f, minutes_dic, "Minutes")
f.close()


f = open(gen_agendas_file, "w")
f.write("""\
Meeting Agendas
===============

.. toctree::
\t:caption: Agendas
\t:maxdepth: 1

""")

agendas_dic = {}
fill_dictionary(agendas_dic, agendas_rst_dir)
write_toc_entries(f, agendas_dic, "Agenda")
f.close()