import json

keycodes = {}

def insert_keycodes(hershey_keycodes, ascii_keycodes):
	print(len(hershey_keycodes), len(ascii_keycodes))
	print(hershey_keycodes, ascii_keycodes)
	for i in range(0, len(ascii_keycodes)):
		print(i)
		keycodes[ascii_keycodes[i]] = hershey_keycodes[i]

data = {'keycodes': keycodes}

hershey_keycodes = [699, 714, 717, 733, 719, 2271, 734, 731, 721, 722, 2219, 725, 711, 724, 710, 720]
ascii_keycodes = list(range(32, 47+1))
insert_keycodes(hershey_keycodes, ascii_keycodes)

hershey_keycodes = list(range(700, 709+1))
ascii_keycodes = list(range(48, 57+1))
insert_keycodes(hershey_keycodes, ascii_keycodes)

hershey_keycodes = [712, 713, 2241, 726, 2242, 715, 2273]
ascii_keycodes = list(range(58, 64+1))
insert_keycodes(hershey_keycodes, ascii_keycodes)

hershey_keycodes = list(range(501, 526+1))
ascii_keycodes = list(range(65, 90+1))
insert_keycodes(hershey_keycodes, ascii_keycodes)

hershey_keycodes = [2223, 804, 2224, 2262, 999, 730]
ascii_keycodes = list(range(91, 96+1))
insert_keycodes(hershey_keycodes, ascii_keycodes)

hershey_keycodes = list(range(601, 626+1))
ascii_keycodes = list(range(97, 122+1))
insert_keycodes(hershey_keycodes, ascii_keycodes)

hershey_keycodes = [2225, 723,  2226, 2246, 718]
ascii_keycodes = list(range(123, 127+1))
insert_keycodes(hershey_keycodes, ascii_keycodes)

with open('hershey-simplex-index.json', 'w') as outfile:
	json.dump(data, outfile, sort_keys=True, indent=4)

