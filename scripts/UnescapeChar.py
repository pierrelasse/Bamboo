inp = input("Enter a 4-digit number: ")

if len(inp) == 4 and inp.isdigit():
    number = int(inp)
    escaped_text = "\\u{}".format(number)
    unescaped_text = bytes(escaped_text, 'utf-8').decode('unicode_escape')
    print(unescaped_text)
else:
    print("Please enter a valid 4-digit number.")
