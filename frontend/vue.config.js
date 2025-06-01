module.exports = {
    devServer: {
        proxy: 'http://localhost:8080',
    },
    configureWebpack: {
        plugins: [
            // Example: Define global constants if needed
            new (require('webpack')).DefinePlugin({
                __VUE_PROD_HYDRATION_MISMATCH_DETAILS__: JSON.stringify(false),
            }),
        ],
    },
};