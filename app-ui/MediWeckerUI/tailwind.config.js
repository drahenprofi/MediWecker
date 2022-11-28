/** @type {import('tailwindcss').Config} */

const defaultTheme = require('tailwindcss/defaultTheme')

module.exports = {
    content: ["./**/*.{razor,html}"],
    safelist: [
        "validation-message"
    ],
    theme: {
        extend: {
            fontFamily: {
                "sans": ['Inter var', ...defaultTheme.fontFamily.sans],
            },
            
            keyframes: {
                'fade-in': {
                    '0%': {
                        opacity: '0'
                    },
                    '100%': {
                        opacity: '1'
                    },
                },
                'fade-move-in': {
                    '0%': {
                        opacity: '0',
                        transform: 'translateX(25px)'
                    },
                    '100%': {
                        opacity: '1',
                        transform: 'translateY(0)'
                    },
                }
            },
            
            animation: {
                'fade-in': 'fade-in 0.2s ease-out',
                'fade-move-in': 'fade-move-in 0.2s ease-out'
            }
        },
    },
    plugins: [
        require('@tailwindcss/forms')
    ],
}
