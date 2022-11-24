/** @type {import('tailwindcss').Config} */

const defaultTheme = require('tailwindcss/defaultTheme')

module.exports = {
    content: ["./**/*.{razor,html}"],
    theme: {
        extend: {
            "fontFamily": {
                "sans": ['Inter var', ...defaultTheme.fontFamily.sans],
            },
        },
    },
    plugins: [
        require('@tailwindcss/forms')
    ],
}
